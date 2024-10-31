package com.codepath.articlesearch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//Import for swipe refresh
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import android.net.Network


fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private val TAG = "MainActivity/"
private val SEARCH_API_KEY = BuildConfig.API_KEY
private val ARTICLE_SEARCH_URL =
    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"


class MainActivity : AppCompatActivity() {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var cacheSwitch: SwitchCompat
    private lateinit var swipeContainer: SwipeRefreshLayout
    private val articles = mutableListOf<DisplayArticle>()
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private val PREFS_NAME = "user_settings"
    private val CACHE_KEY = "enable_cache"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        setupNetworkCallBack()


        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val cacheEnabled = getPreferences(Context.MODE_PRIVATE).getBoolean("cacheEnabled", true)
        if (!cacheEnabled) {
            lifecycleScope.launch(IO) {
                (application as ArticleApplication).db.articleDao().deleteAll()
            }
        }
        cacheSwitch = findViewById(R.id.switch_cache)
        cacheSwitch.isChecked = sharedPreferences.getBoolean(CACHE_KEY,true)
        cacheSwitch.setOnCheckedChangeListener{ _, isChecked ->
            with(sharedPreferences.edit()){
                putBoolean(CACHE_KEY,isChecked)
                apply()
            }

            Toast.makeText(this,if(isChecked) "Articles available offline" else "Newer Articles will not be saved",Toast.LENGTH_SHORT).show()
        }

        articlesRecyclerView = findViewById(R.id.articles)
        swipeContainer = findViewById(R.id.swipeContainer)

        articleAdapter = ArticleAdapter(this, articles)
        articlesRecyclerView.adapter = articleAdapter
        articlesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        swipeContainer.setOnRefreshListener {
            Log.d(TAG, "SWIPE MOTION")
            Log.d(TAG,"${isNetworkConnected()} , ${cacheSwitch.isChecked}")
            if (isNetworkConnected()) {
                fetchArticles(articleAdapter,this)
            } else {
                if(!cacheSwitch.isChecked && !isNetworkConnected()) {
                    lifecycleScope.launch(IO) {
                        (application as ArticleApplication).db.articleDao().deleteAll()
                    }
                }
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                swipeContainer.isRefreshing = false
            }
        }

        lifecycleScope.launch {
            (application as ArticleApplication).db.articleDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayArticle(
                        entity.headline,
                        entity.articleAbstract,
                        entity.byline,
                        entity.mediaImageUrl
                    )
                }.also { mappedList ->
                    articles.clear()
                    articles.addAll(mappedList)
                    articleAdapter.notifyDataSetChanged()
                }
            }
        }
        if (isNetworkConnected()) {
            fetchArticles(articleAdapter,this)
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            swipeContainer.isRefreshing = false
        }
    }
    private fun setupNetworkCallBack(){
        val networkRequest = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Automatically refresh articles when connection is restored
                runOnUiThread {
                    fetchArticles(articleAdapter, this@MainActivity)
                    Toast.makeText(this@MainActivity, "Connection restored, refreshing articles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onLost(network: Network) {
                // Notify user of connection loss if needed
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Connection lost", Toast.LENGTH_SHORT).show()
                }
            }
        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
    private fun fetchArticles(articleAdapter: ArticleAdapter,context:Context) {
        val client = AsyncHttpClient()
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val cachingEnabled = sharedPreferences.getBoolean(CACHE_KEY,true)
        client.get(ARTICLE_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch articles: $statusCode, $headers, $response, $throwable")
                swipeContainer.isRefreshing = false
            }
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val parsedJson = createJson().decodeFromString(
                        SearchNewsResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    parsedJson.response?.docs?.let { list ->
                        Log.d(TAG,"ARE WE SAVING: $cachingEnabled")
                        articles.clear()
                        articles.addAll(list.map{
                            DisplayArticle(
                                headline = it.headline?.main,
                                abstract = it.abstract,
                                byline = it.byline?.original,
                                mediaImageUrl = it.mediaImageUrl
                            )
                        })

                        if(cachingEnabled){
                            lifecycleScope.launch(IO) {
                                (application as ArticleApplication).db.articleDao().deleteAll()
                                (application as ArticleApplication).db.articleDao().insertAll(list.map {
                                    ArticleEntity(
                                        headline = it.headline?.main,
                                        articleAbstract = it.abstract,
                                        byline = it.byline?.original,
                                        mediaImageUrl = it.mediaImageUrl
                                    )
                                })
                            }
                        }
                        else{
                            lifecycleScope.launch(IO) {
                                (application as ArticleApplication).db.articleDao().deleteAll()
                            }
                        }

                    }
                    Log.d(TAG,"RESPONSE: $parsedJson")
                    articleAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                } finally {
                    Toast.makeText(context, "Your articles are up to date", Toast.LENGTH_SHORT).show()
                    swipeContainer.isRefreshing = false
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}