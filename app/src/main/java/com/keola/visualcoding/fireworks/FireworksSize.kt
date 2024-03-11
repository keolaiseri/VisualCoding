package com.keola.visualcoding.fireworks

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.R
import com.keola.visualcoding.R.id.fireworksSurfaceView

class FireworksSize: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fireworks_size)
        Log.d("FireworksSize", "onCreate called")

        val selectedColor = intent.getIntExtra("selectedColor", 0)
        Log.d("FireworksSize", "Selected color: $selectedColor")
        val fireworksView: FireworksView = findViewById(fireworksSurfaceView)
        fireworksView.selectedColor = selectedColor

        val continueSizeButtonSize: Button = findViewById(R.id.continue_fireworks_size_Button)
        continueSizeButtonSize.isEnabled = false // Disable the button until a size is entered
        continueSizeButtonSize.setOnClickListener {
            if (continueSizeButtonSize.isEnabled) {
                // Write code to start the FireworksActivity
                val intent = Intent(this, FireworksFinishedActivity::class.java)
                intent.putExtra("selectedColor", selectedColor)
                intent.putExtra("selectedSize", fireworksView.particleSize)
                startActivity(intent)
            } else {
                Toast.makeText(this@FireworksSize, "Please enter a valid size before proceeding", Toast.LENGTH_SHORT).show()
            }
        }


        val particleSizeInput: EditText = findViewById(R.id.particleSizeInput)
        particleSizeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val size = s.toString().toIntOrNull()
                if (size != null && size in 1..10) {
                    fireworksView.particleSize = size
                    continueSizeButtonSize.isEnabled = true //enable the button
                } else {
                    particleSizeInput.error = "Please enter a number between 1 and 10"
                    continueSizeButtonSize.isEnabled = false //disable the button
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        // Touch listener for triggering fireworks
        findViewById<View>(R.id.fireworksSurfaceView).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y
                    fireworksView.addFirework(x, y)
                    Log.d("FireworksSize", "Firework added at $x, $y")
                    return@setOnTouchListener true
                }
                else -> false
            }
        }

    }


}