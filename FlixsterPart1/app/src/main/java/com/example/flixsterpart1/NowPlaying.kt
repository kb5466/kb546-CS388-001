package com.example.flixsterpart1
import com.google.gson.annotations.SerializedName;
class NowPlaying {
    @SerializedName("original_title")
    val movie_title: String? = null

    @SerializedName("overview")
    val overview: String? = null

    @SerializedName("poster_path")
    val imgurl: String? = null

    @SerializedName("vote_average")
    val vote: Double = 0.0

}