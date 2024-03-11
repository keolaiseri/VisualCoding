package com.keola.visualcoding.fireworks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.HomeScreenActivity
import com.keola.visualcoding.R

class FireworksFinishedActivity : AppCompatActivity()
{
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var fireworksView: FireworksView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fireworks_finished)

        val selectedColor = intent.getIntExtra("selectedColor", 0)
        Log.d("FireworksFinished", "Selected color: $selectedColor")
        val selectedSize = intent.getIntExtra("selectedSize", 0)
        Log.d("FireworksFinished", "Selected size: $selectedSize")


        fireworksView = findViewById(R.id.fireworksSurfaceView)
        fireworksView.selectedColor = selectedColor
        fireworksView.particleSize = selectedSize

        handler.post(object : Runnable {
            override fun run() {
                val x = (0..fireworksView.width).random().toFloat()
                val y = (0..fireworksView.height).random().toFloat()
                fireworksView.addFirework(x, y)
                handler.postDelayed(this, 2000) // repeat every 2 seconds
            }
        })


        val continueFinishedButton: Button = findViewById(R.id.continue_fireworks_finished_Button)
        continueFinishedButton.setOnClickListener {

            // Set the hasFinished flag in SharedPreferences to true
            val sharedPreferences = getSharedPreferences("FireworksPrefs", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putBoolean("hasFinished", true)
                apply()
            }

            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // stop repeating when activity is destroyed
    }

    fun updateFireworks(size: Float, color: Int) {
        // Assuming you have a reference to the FireworksView
        fireworksView.particleSize = size.toInt()
        fireworksView.selectedColor = color
    }
}