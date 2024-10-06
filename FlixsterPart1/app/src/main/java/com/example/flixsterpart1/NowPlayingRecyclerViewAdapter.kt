package com.example.flixsterpart1

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class NowPlayingRecyclerViewAdapter(
    private val movies: List<NowPlaying>,
    private val mListener: OnListFragmentInteractionListener?
): RecyclerView.Adapter<NowPlayingRecyclerViewAdapter.MovieHolder>() {

    inner class MovieHolder(val mView: View): RecyclerView.ViewHolder(mView){
        var mItem: NowPlaying? = null
        val mMovieImage: ImageView = mView.findViewById(R.id.movie_image) as ImageView
        val mMovieTitle: TextView = mView.findViewById(R.id.movie_title) as TextView
        val mMovieOverview: TextView = mView.findViewById(R.id.movie_overview) as TextView
        val mMovieVote: TextView = mView.findViewById(R.id.movie_vote) as TextView
        override fun toString(): String {
            return mMovieTitle.toString() + " '" + mMovieVote.text + "'"
        }
    }
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movies[position]

        holder.mItem = movie
        val movieRating = "Rating: ${movie.vote}/10"

        val imgUrl = "https://image.tmdb.org/t/p/w500/${movie.imgurl}"
        holder.mMovieTitle.text = movie.movie_title
        holder.mMovieOverview.text = movie.overview
        holder.mMovieVote.text = movieRating

        val placeHolderImg = R.drawable.ic_launcher_foreground
        Glide.with(holder.mView)
            .load(imgUrl)
            .apply(RequestOptions().placeholder(placeHolderImg))
            .centerInside()
            .into(holder.mMovieImage)
        holder.mView.setOnClickListener{
            holder.mItem?.let {mov->
                mListener?.onItemClick(mov)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_now_playing, parent, false)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int {
       return movies.size
    }


}