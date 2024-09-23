package com.example.wishlist

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    var wishes: MutableList<wishlist> = mutableListOf()
    lateinit var adapter: wishlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wishlistRv = findViewById<RecyclerView>(R.id.wishlistRv)
        val itemWish = findViewById<TextInputEditText>(R.id.itemWish)
        val priceWish = findViewById<TextInputEditText>(R.id.priceWish)
        val urlWish = findViewById<TextInputEditText>(R.id.urlWish)
        val submit = findViewById<Button>(R.id.submit)

        adapter = wishlistAdapter(wishes) { wishlistItem, position ->

            wishes.removeAt(position)
            adapter.notifyItemRemoved(position)

            // notify adapter so that it doesnt go out of bounds
            adapter.notifyItemRangeChanged(position,wishes.size)
        }

        wishlistRv.adapter = adapter
        wishlistRv.layoutManager = LinearLayoutManager(this)

        submit.setOnClickListener {
            val wish = wishlist(
                itemWish.text.toString(),
                priceWish.text.toString(),
                urlWish.text.toString()
            )

            // Add new item to the list
            wishes.add(wish)

            // Clear input fields after submitting
            itemWish.setText("")
            priceWish.setText("")
            urlWish.setText("")

            // Notify the adapter that a new item is added
            adapter.notifyItemInserted(wishes.size - 1) // Notify RecyclerView about the inserted item
            wishlistRv.scrollToPosition(wishes.size - 1) // Scroll to the newly added item
        }
    }
}