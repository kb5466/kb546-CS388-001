package com.example.wishlist

import android.text.util.Linkify
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View;

import android.widget.TextView;
import androidx.appcompat.view.menu.MenuView.ItemView

class wishlistAdapter(
    private val wishlists: MutableList<wishlist>,
    private val onItemLongClick: (wishlist, Int) -> Unit // int for the position of the wish in the wishlist to be removed
) : RecyclerView.Adapter<wishlistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView
        val costTextView: TextView
        val urlTextView: TextView

        init{
            itemTextView = itemView.findViewById(R.id.itemText)
            costTextView = itemView.findViewById(R.id.costText)
            urlTextView = itemView.findViewById(R.id.urlText)
        }
        // Bind method for long click
        fun bind(wishlist: wishlist, position: Int, onItemLongClick: (wishlist, Int) -> Unit) {
            itemView.setOnLongClickListener {
                onItemLongClick(wishlist, position) // listener for the wish
                true // Return true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wishlistAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.wish_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: wishlistAdapter.ViewHolder, position: Int) {
        val wishlist = wishlists[position]
        holder.itemTextView.text = wishlist.item
        holder.costTextView.text = wishlist.price
        holder.urlTextView.text = wishlist.url

        // Bind the long click listener to the ViewHolder
        holder.bind(wishlist, position, onItemLongClick)
    }

    override fun getItemCount(): Int {
        return wishlists.size
    }
}