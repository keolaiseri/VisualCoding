package com.keola.visualcoding.tunnel

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.HomeScreenActivity
import com.keola.visualcoding.R


class TunnelArtActivity: AppCompatActivity(){

    // Define stars as a list of Star objects
    private var stars = ArrayList<Star>()

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
        setContentView(R.layout.activity_tunnel_art)
        val tunnelView = findViewById<TunnelView>(R.id.tunnelView) // replace "tunnelView" with the actual ID of your TunnelView in the layout file

        stars = generateStars()

        val imageResId = intent.getIntExtra("imageResId", 0)

        val shapeSpinner = findViewById<Spinner>(R.id.shapeSpinner)


        // Retrieve the saved values from SharedPreferences
        val sharedPreferences = getSharedPreferences("TunnelArtPrefs", Context.MODE_PRIVATE)
        val shape = sharedPreferences.getString("shape", "star")
        val color = sharedPreferences.getInt("color", Color.YELLOW)
        val size = sharedPreferences.getFloat("size", 5f)
        val speed = sharedPreferences.getFloat("speed", 5f)

        // Set the initial values of shape, color, size, and speed
        tunnelView.shape = shape ?: "star"
        tunnelView.color = color
        tunnelView.size = size
        tunnelView.speed = speed


        val adapter = ArrayAdapter.createFromResource(this,
            R.array.shapes_array,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        shapeSpinner.adapter = adapter

        /*
        val continueButton: Button = findViewById(R.id.continue_tunnel_Button)
        continueButton.setOnClickListener {
            val intent = Intent(this, EditTunnelArtActivity::class.java)
           intent.putExtra("shape", tunnelView.shape)
            intent.putExtra("color", tunnelView.color)
            intent.putExtra("size", tunnelView.size)
            intent.putExtra("speed", tunnelView.speed)
            startActivity(intent)
        }*/

        shapeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                tunnelView.shape = parent.getItemAtPosition(position).toString()
                Log.d("TunnelArtActivity", "Shape selected: ${tunnelView.shape}")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val colorInput = findViewById<EditText>(R.id.colorInput)
        colorInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val color = try {
                    if (s.toString().isEmpty()) {
                        Color.YELLOW // default to yellow if the input field is empty
                    } else {
                        Color.parseColor(s.toString())
                    }
                } catch (e: IllegalArgumentException) {
                    Log.e("TunnelArtActivity", "Invalid color code: ${s.toString()}")
                    Color.YELLOW // default to yellow if the entered hex code is invalid
                }
                tunnelView.color = color
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val sizeInput = findViewById<EditText>(R.id.sizeInput)
        sizeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val size = try {
                    val inputSize = s.toString().toFloat()
                    if (inputSize in 1.0..10.0) inputSize else 5f
                } catch (e: NumberFormatException) {
                    Log.e("TunnelArtActivity", "Invalid size: ${s.toString()}")
                    5f // default size if the entered size is invalid
                }
                tunnelView.size = size
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val speedInput = findViewById<EditText>(R.id.speedInput)
        speedInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val speed = try {
                    val inputSpeed = s.toString().toFloat()
                    if (inputSpeed in 1.0..10.0) inputSpeed else 5f
                } catch (e: NumberFormatException) {
                    Log.e("TunnelArtActivity", "Invalid speed: ${s.toString()}")
                    5f // default speed if the entered speed is invalid
                }
                tunnelView.speed = speed
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val completeButton: Button = findViewById(R.id.complete_tunnel_Button)
        completeButton.setOnClickListener {
            // Save the current values to SharedPreferences
            val sharedPreferences = getSharedPreferences("TunnelArtPrefs", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putString("shape", tunnelView.shape)
                putInt("color", tunnelView.color)
                putFloat("size", tunnelView.size)
                putFloat("speed", tunnelView.speed)
                putString("colorInputText", colorInput.text.toString())
                putString("sizeInputText", sizeInput.text.toString())
                putString("speedInputText", speedInput.text.toString())
                putInt("shapeSpinnerPosition", shapeSpinner.selectedItemPosition)
                apply()
            }

            // Clear the input fields
            colorInput.setText("")
            sizeInput.setText("")
            speedInput.setText("")

            // Navigate back to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }


    }

    // This is a placeholder method. Replace it with your actual code for creating the Star objects.
    private fun generateStars(): ArrayList<Star> {
        // Generate your Star objects and add them to the list
        return ArrayList<Star>()
    }

    fun toggleFullscreen(view: View) {
        val fullscreenButton = findViewById<ImageButton>(R.id.fullscreenButton)
        val tunnelView = findViewById<TunnelView>(R.id.tunnelView)

        // Get references to all the views that you want to hide
        val tunnelText = findViewById<TextView>(R.id.drawText)
        val linearLayout1 = findViewById<LinearLayout>(R.id.linearLayout1)
        val linearLayout2 = findViewById<LinearLayout>(R.id.linearLayout2)
        val linearLayout3 = findViewById<LinearLayout>(R.id.linearLayout3)
        val linearLayout4 = findViewById<LinearLayout>(R.id.linearLayout4)
        val continueButton = findViewById<Button>(R.id.complete_tunnel_Button)

        if (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
            // Go to fullscreen mode
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            actionBar?.hide()
            tunnelView.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            fullscreenButton.setImageResource(R.drawable.exit_fullscreen) // Change button icon to 'exit fullscreen'

            // Hide all other views
            tunnelText.visibility = View.GONE
            linearLayout1.visibility = View.GONE
            linearLayout2.visibility = View.GONE
            linearLayout3.visibility = View.GONE
            linearLayout4.visibility = View.GONE
            continueButton.visibility = View.GONE

            tunnelView.isFullscreen = true
        } else {
            // Exit fullscreen mode
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            actionBar?.show()
            tunnelView.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            fullscreenButton.setImageResource(R.drawable.fullscreen) // Change button icon back to 'fullscreen'

            // Show all other views
            tunnelText.visibility = View.VISIBLE
            linearLayout1.visibility = View.VISIBLE
            linearLayout2.visibility = View.VISIBLE
            linearLayout3.visibility = View.VISIBLE
            linearLayout4.visibility = View.VISIBLE
            continueButton.visibility = View.VISIBLE

            tunnelView.isFullscreen = false

        }
    }
}
