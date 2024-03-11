package com.keola.visualcoding

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keola.visualcoding.draw.DrawingActivity
import com.keola.visualcoding.fractal.FractalActivity
import com.keola.visualcoding.rain.RainActivity
import com.keola.visualcoding.spiral.SpiralActivity
import com.keola.visualcoding.tunnel.TunnelArtActivity
import com.keola.visualcoding.writing.WritingActivity

class GridAdapter(private val items: List<GridItem>, private val context: Context) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageButton: ImageButton = view.findViewById(R.id.imageButton)
        val textView: TextView = view.findViewById(R.id.textView)

        init {
            imageButton.post {
                val layoutParams = imageButton.layoutParams
                layoutParams.height = imageButton.width
                imageButton.layoutParams = layoutParams
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageButton.setImageResource(item.imageResId)
        // Set the text for each item
        holder.imageButton.setImageResource(when (position) {
            0 -> R.drawable.star_button
            1 -> R.drawable.fractal_button
            2 -> R.drawable.drawing_button
            3 -> R.drawable.writing_button
            4 -> R.drawable.rain_button
            5 -> R.drawable.spiral_button
            else -> R.drawable.image1_background
        })
        holder.textView.text = when (position) {
            0 -> "Tunnel"
            1 -> "Fractal"
            2 -> "Drawing"
            3 -> "Writing"
            4 -> "Rain"
            5 -> "Spiral"
            else -> "Other"
        }
        holder.imageButton.setOnClickListener {
            val intent = when (position) {
                0 -> Intent(context, TunnelArtActivity::class.java)
                1 -> Intent(context, FractalActivity::class.java)
                2 -> Intent(context, DrawingActivity::class.java)
                3 -> Intent(context, WritingActivity::class.java)
                4 -> Intent(context, RainActivity::class.java)
                5 -> Intent(context, SpiralActivity::class.java)
                else -> Intent(context, DetailActivity::class.java)
            }
            intent.putExtra("imageResId", item.imageResId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}