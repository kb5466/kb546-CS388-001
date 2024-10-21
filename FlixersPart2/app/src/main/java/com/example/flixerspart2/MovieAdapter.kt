package com.example.flixerspart2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.flixerspart2.ShowAdapter.ViewHolder

const val MOVIE_EXTRA = "MOVIE_EXTRA"
class MovieAdapter (private val context: Context, private val movies: List<Movie>):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_show, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        private val showImage = itemView.findViewById<ImageView>(R.id.show_img);
        private val showRating = itemView.findViewById<TextView>(R.id.show_rating);
        init{
            itemView.setOnClickListener(this)
        }
        fun bind(Movie: Movie){
            showRating.text = Movie.movie_vote + " / 10"

            val radius = 30;
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500${Movie.movie_poster}")
                .transform(RoundedCorners(radius))
                .into(showImage)
        }

        override fun onClick(v: View?) {
            val movie = movies[adapterPosition]
            val intent = Intent(context,MovieDetail::class.java)
            intent.putExtra(MOVIE_EXTRA,movie)
            context.startActivity(intent)
        }
    }
}
