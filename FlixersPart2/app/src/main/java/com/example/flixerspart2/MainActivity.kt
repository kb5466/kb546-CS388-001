package com.example.flixerspart2

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixerspart2.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONException
import org.json.JSONObject

private val TAG = "MainActivity/"
private val SEARCH_API_KEY = BuildConfig.api_key
private val SHOW_URL =
    "https://api.themoviedb.org/3/tv/top_rated?&api_key=$SEARCH_API_KEY"

private val MOVIE_URL =
    "https://api.themoviedb.org/3/movie/top_rated?&api_key=$SEARCH_API_KEY"

class MainActivity : AppCompatActivity() {
    private val shows = mutableListOf<Show>()
    private val movies = mutableListOf<Movie>()

    private lateinit var showRecyclerView: RecyclerView
    private lateinit var movieRecyclerView: RecyclerView

    private lateinit var binding: ActivityMainBinding

    private val showAdapter = ShowAdapter(this, shows)
    private val movieAdapter = MovieAdapter(this, movies)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize RecyclerView using binding
        showRecyclerView = binding.showList  // Access the RecyclerView from your layout file (ensure the ID matches)
        showRecyclerView.adapter = showAdapter
        showRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        movieRecyclerView = binding.movieList  // Access the RecyclerView from your layout file (ensure the ID matches)
        movieRecyclerView.adapter = movieAdapter
        movieRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Fetch data from the API
        fetchShows()
        fetchMovies()
    }
    private fun fetchMovies() {
        val client = AsyncHttpClient()
        client.get(MOVIE_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch movies: $statusCode")  // Updated the log message
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                try {
                    val jsonObject = json?.jsonObject ?: return
                    val resultsArray = jsonObject.getJSONArray("results")
                    println(resultsArray)
                    // Use Gson to parse the list of movies
                    val gson = Gson()
                    val movieListType = object : TypeToken<List<Movie>>() {}.type
                    val movieList: List<Movie> = gson.fromJson(resultsArray.toString(), movieListType)

                    // Add the movies to the adapter's data set
                    movies.addAll(movieList)  // Ensure you're using the correct list for movies
                    movieAdapter.notifyDataSetChanged()  // Ensure you're using the correct adapter for movies

                } catch (e: JSONException) {
                    Log.e(TAG, "Failed to parse JSON", e)
                }
            }
        })
    }


    private fun fetchShows() {
        val client = AsyncHttpClient()
        client.get(SHOW_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch shows: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                try {
                    val jsonObject = json?.jsonObject ?: return
                    val resultsArray = jsonObject.getJSONArray("results")


                    // Use Gson to parse the list of shows
                    val gson = Gson()
                    val showListType = object : TypeToken<List<Show>>() {}.type
                    val showList: List<Show> = gson.fromJson(resultsArray.toString(), showListType)

                    // Add the shows to the adapter's data set
                    shows.addAll(showList)
                    showAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Log.e(TAG, "Failed to parse JSON", e)
                }
            }
        })
    }
}
