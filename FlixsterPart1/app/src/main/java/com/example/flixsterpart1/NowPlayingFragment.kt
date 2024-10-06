package com.example.flixsterpart1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONObject

class NowPlayingFragment: Fragment(), OnListFragmentInteractionListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing_list,container,false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context,1)
        updateAdapter(progressBar,recyclerView)
        return view
    }
    private fun updateAdapter(progressBar: ContentLoadingProgressBar,recyclerView: RecyclerView){
        progressBar.show()
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"]=BuildConfig.api_key

        client[
            "https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1",
            params,
            object: JsonHttpResponseHandler()
            {
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String,
                    t: Throwable?
                ) {
                    progressBar.hide()

                    // If the error is not null, log it!
                    t?.message?.let {
                        println(params.toString())
                        Log.e("Movie", response)
                    }
                }

                override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                    progressBar.hide()

                    // Convert the "results" JSON array to a string for Gson deserialization
                    val resultsJSON = json.jsonObject.getJSONArray("results").toString()

                    // Define the list type to deserialize JSON into a List<NowPlaying>
                    val arrayMovieType = object : TypeToken<List<NowPlaying>>() {}.type
                    val gson = Gson()
                    val models: List<NowPlaying> = gson.fromJson(resultsJSON, arrayMovieType)

                    // Set up RecyclerView adapter
                    recyclerView.adapter = NowPlayingRecyclerViewAdapter(models, this@NowPlayingFragment)  // Ensure the adapter constructor matches `this` or specify a valid context

                    Log.d("NowPlayingFragment", json.toString())
                }

            }
        ]
    }
    override fun onItemClick(item: NowPlaying) {
        Toast.makeText(context,"test: "+item.movie_title, Toast.LENGTH_SHORT).show()
    }

}