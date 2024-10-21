package com.example.flixerspart2

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
class Movie : Serializable {
    @SerializedName("original_title")
    val movie_name: String? = null

    @SerializedName("overview")
    val movie_overview: String? = null

    @SerializedName("poster_path")
    val movie_poster: String? = null

    @SerializedName("backdrop_path")
    val movie_backdrop: String? = null

    @SerializedName("vote_average")
    val movie_vote: String? = null
}