package com.example.flixerspart2

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
class Show : Serializable {
    @SerializedName("original_name")
    val show_name: String? = null

    @SerializedName("overview")
    val show_overview: String? = null

    @SerializedName("poster_path")
    val show_poster: String? = null

    @SerializedName("backdrop_path")
    val show_backdrop: String? = null

    @SerializedName("vote_average")
    val show_vote: String? = null
}

