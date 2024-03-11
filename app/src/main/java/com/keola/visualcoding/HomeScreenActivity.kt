package com.keola.visualcoding

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keola.visualcoding.tunnel.TunnelArtActivity

class HomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val spanCount = 2 // number of grid columns
        val spacing = 100 // spacing between grid items
        val includeEdge = true // include edge spacing


        recyclerView.layoutManager = GridLayoutManager(this, spanCount)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))

        val items = listOf(
            GridItem(R.drawable.image1_background),
            GridItem(R.drawable.image1_background),
            GridItem(R.drawable.image1_background),
            GridItem(R.drawable.image1_background),
            GridItem(R.drawable.image1_background),
            GridItem(R.drawable.image1_background)
            // Add more items as needed
        )

        recyclerView.adapter = GridAdapter(items, this)    }
}