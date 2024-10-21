package com.example.flixerspart2

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

const val SHOW_EXTRA = "SHOW_EXTRA"

class ShowAdapter(private val context: Context, private val shows: List<Show>):
    RecyclerView.Adapter<ShowAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_show, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowAdapter.ViewHolder, position: Int) {
        val show = shows[position]
        holder.bind(show)
    }

    override fun getItemCount() = shows.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
            private val showImage = itemView.findViewById<ImageView>(R.id.show_img);
            private val showRating = itemView.findViewById<TextView>(R.id.show_rating);
            init{
                itemView.setOnClickListener(this)
            }
            fun bind(Show: Show){
                showRating.text = Show.show_vote + " / 10"

                val radius = 30;
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500${Show.show_poster}")
                    .transform(RoundedCorners(radius))
                    .into(showImage)
            }

        override fun onClick(v: View?) {
            val show = shows[adapterPosition]
            val intent = Intent(context,Show_detail::class.java)
            intent.putExtra(SHOW_EXTRA,show)
            context.startActivity(intent)
        }
    }
}