package com.keola.visualcoding.rain

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
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
import java.util.Random

class RainActivity : AppCompatActivity() {

    private lateinit var rainView: RainView
    private val handler = Handler()
    private val random = Random()
    private var mediaPlayer: MediaPlayer? = null

    private var downX: Float = 0f

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
        setContentView(R.layout.activity_rain)

        rainView = findViewById(R.id.rainView)


        val thunderSpinner = findViewById<Spinner>(R.id.thunderSpinner)
        thunderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "True") {
                    // If "True" is selected, start playing the thunder sound
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this@RainActivity, R.raw.thunder_sound)
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.setVolume(1.0f, 1.0f) // Set volume to maximum

                    mediaPlayer?.start()
                } else {
                    // If "False" is selected, stop playing the thunder sound
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.true_false_array, // This is an array in strings.xml containing "True" and "False"
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            thunderSpinner.adapter = adapter
        }

        // Set the default selected item to "False"
        thunderSpinner.setSelection(1) // 1 is the position of "False" in the array

        // Add a TextWatcher to the speedRainInput EditText
        val speedInput = findViewById<EditText>(R.id.speedRainInput)
        speedInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // When the text changes, update the speed of the rain
                val speed = s.toString().toFloatOrNull()
                if (speed != null && speed in 1.0..10.0) {
                    // Multiply the speed by 5 before setting it to the RainView
                    rainView.setRainSpeed(speed * 20)
                } else {
                    // If the input is invalid or not in the range 1.0 to 10.0, set the speed to the default value of 5
                    rainView.setRainSpeed(100f)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        // Defer the addition of the water droplet until the RainView has been laid out
        rainView.post {
            val x = (0 until rainView.width).random().toFloat()
            val y = (0 until rainView.height).random().toFloat()
            val size = random.nextFloat() * 40 + 40 // 10 to 20 pixels
            rainView.addWaterDroplet(x, y, size)
        }

        // Schedule the addition of a new water droplet every 5 seconds
        handler.postDelayed(object : Runnable {
            override fun run() {
                val x = (0 until rainView.width).random().toFloat()
                val y = (0 until rainView.height).random().toFloat()
                val size = random.nextFloat() * 40 + 40 // 10 to 20 pixels
                rainView.addWaterDroplet(x, y, size)

                handler.postDelayed(this, 5000) // 5 seconds
            }
        }, 5000) // Initial delay of 5 seconds

        // Start the animation loop
        handler.post(object : Runnable {
            override fun run() {

                val dt = 1f / 60 // Time delta for 60 frames per second

                if (random.nextFloat() < 0.5) { // 50% chance to spawn a raindrop each frame
                    val x = (0 until rainView.width).random().toFloat()
                    rainView.addRaindrop(x)
                }
                rainView.invalidate()
                rainView.update(1f / 60) // Update the system 60 times per second
                handler.postDelayed(this, 1000 / 60) // 60 frames per second


            }
        })

        // Add a TextWatcher to the windSpeedInput EditText
        val windSpeedInput = findViewById<EditText>(R.id.windSpeedInput)
        windSpeedInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // When the text changes, update the wind speed of the rain
                val windSpeed = s.toString().toFloatOrNull()
                if (windSpeed != null && windSpeed in -10.0..10.0) {
                    // Multiply the wind speed by 5 before setting it to the RainView
                    rainView.setWindSpeed(windSpeed * 5)
                } else {
                    // If the input is invalid or not in the range -10.0 to 10.0, set the wind speed to the default value of 0
                    rainView.setWindSpeed(0f)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        val completeButton: Button = findViewById(R.id.complete_rain_Button)
        completeButton.setOnClickListener {
            // Navigate back to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

    }

}