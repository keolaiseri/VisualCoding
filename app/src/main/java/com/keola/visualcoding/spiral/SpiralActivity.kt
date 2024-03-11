package com.keola.visualcoding.spiral

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.HomeScreenActivity
import com.keola.visualcoding.R

class SpiralActivity: AppCompatActivity() {

    private var downX: Float = 0f
    private lateinit var spiralView: SpiralView


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                return true
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = event.x - downX
                if (Math.abs(deltaX) > 150) { // MIN_DISTANCE is an arbitrary choice to detect a swipe
                    if (deltaX > 0) { // Right to left swipe
                        this.onBackPressed()
                        return true
                    }
                }
                return super.onTouchEvent(event)
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spiral)

        spiralView = findViewById(R.id.spiralView)

        // Retrieve the saved values from SharedPreferences
        val sharedPreferences = getSharedPreferences("SpiralPrefs", Context.MODE_PRIVATE)
        val color = sharedPreferences.getInt("color", Color.BLACK)
        val size = sharedPreferences.getFloat("size", 1f)
        val speed = sharedPreferences.getFloat("speed", 1f)
        val strokeWidth = sharedPreferences.getFloat("strokeWidth", 5f)

        // Set the retrieved values to the SpiralView
        spiralView.color = color
        spiralView.size = size
        spiralView.speed = speed
        spiralView.strokeWidth = strokeWidth

        val spiralInput = findViewById<EditText>(R.id.spiralInput)
        spiralInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val colorString = s.toString()
                try {
                    val color = Color.parseColor(colorString)
                    spiralView.color = color
                } catch (e: IllegalArgumentException) {
                    // The color string is not a valid color, handle this case as you see fit
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        val speedSpiralInput = findViewById<EditText>(R.id.speedSpiralInput)
        speedSpiralInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val speedString = s.toString()
                try {
                    val speedInput = speedString.toInt()
                    if (speedInput in 1..10) {
                        spiralView.speed = speedInput.toFloat()
                    } else {
                        // The speed is not between 1 and 10, handle this case as you see fit
                    }
                } catch (e: NumberFormatException) {
                    // The speed string is not a valid integer, handle this case as you see fit
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        val strokeWidthInput = findViewById<EditText>(R.id.strokeSpiralInput)
        strokeWidthInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val strokeWidthString = s.toString()
                try {
                    val strokeSpiralInput = strokeWidthString.toFloat()
                    if (strokeSpiralInput in 1f..10f) {
                        spiralView.strokeWidth = strokeSpiralInput
                    } else {
                        // The stroke width is not between 1 and 10, handle this case as you see fit
                    }
                } catch (e: NumberFormatException) {
                    // The stroke width string is not a valid float, handle this case as you see fit
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        val completeButton: Button = findViewById(R.id.complete_spiral_Button)
        completeButton.setOnClickListener {
            // Save the current values to SharedPreferences
            val sharedPreferences = getSharedPreferences("SpiralPrefs", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putInt("color", spiralView.color)
                putFloat("size", spiralView.size)
                putFloat("speed", spiralView.speed)
                putFloat("strokeWidth", spiralView.strokeWidth)
                apply()
            }

            // Navigate back to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }



    }

}