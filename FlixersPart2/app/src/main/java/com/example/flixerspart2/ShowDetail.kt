package com.example.flixerspart2

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class Show_detail : AppCompatActivity() {
    private lateinit var showPoster: ImageView
    private lateinit var showBackdrop: ImageView
    private lateinit var showTitle: TextView
    private lateinit var showRating: TextView
    private lateinit var showDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_show_detail)

        // Initialize UI elements
        showPoster = findViewById(R.id.show_poster)
        showBackdrop = findViewById(R.id.show_backdrop)
        showTitle = findViewById(R.id.show_title)
        showRating = findViewById(R.id.show_rate)
        showDescription = findViewById(R.id.show_desc)

        // Retrieve the Show object passed from the adapter
        val show = intent.getSerializableExtra(SHOW_EXTRA) as? Show
        if (show != null) {
            // Display the show details
            showTitle.text = show.show_name
            showRating.text = "${show.show_vote} / 10"
            showDescription.text = show.show_overview

            // Load images with Glide
            val radius = 30;
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${show.show_poster}")
                .transform(RoundedCorners(radius))
                .into(showPoster)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${show.show_backdrop}")
                .into(showBackdrop)
        }
    }
}
