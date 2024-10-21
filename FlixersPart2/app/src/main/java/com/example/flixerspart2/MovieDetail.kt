package com.example.flixerspart2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MovieDetail: AppCompatActivity() {
    private lateinit var moviePoster: ImageView
    private lateinit var movieBackdrop: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieRating: TextView
    private lateinit var movieDescription: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_movie_detail)

        // Initialize UI elements
        moviePoster = findViewById(R.id.movie_poster)
        movieBackdrop = findViewById(R.id.movie_backdrop)
        movieTitle = findViewById(R.id.movie_title)
        movieRating = findViewById(R.id.movie_rate)
        movieDescription = findViewById(R.id.movie_desc)

        // Retrieve the movie object passed from the adapter
        val movie = intent.getSerializableExtra(MOVIE_EXTRA) as? Movie
        if (movie != null) {
            // Display the movie details
            movieTitle.text = movie.movie_name
            movieRating.text = "${movie.movie_vote} / 10"
            movieDescription.text = movie.movie_overview

            // Load images with Glide
            val radius = 30;
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${movie.movie_poster}")
                .transform(RoundedCorners(radius))
                .into(moviePoster)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${movie.movie_backdrop}")
                .into(movieBackdrop)
        }
    }
}