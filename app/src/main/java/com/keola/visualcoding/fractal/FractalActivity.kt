package com.keola.visualcoding.fractal

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.HomeScreenActivity
import com.keola.visualcoding.R

class FractalActivity : AppCompatActivity() {


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
        setContentView(R.layout.activity_fractal)

        val fractalView = findViewById<FractalView>(R.id.fractalView)

        // Retrieve the saved values from SharedPreferences
        val sharedPreferences = getSharedPreferences("FractalArtPrefs", Context.MODE_PRIVATE)
        val shape = sharedPreferences.getString("shape", "Star")
        val colorSchemePosition = sharedPreferences.getInt("colorScheme", 0)
        val branchWidth = sharedPreferences.getFloat("branchWidth", 8f)
        val fractalSpeed = sharedPreferences.getFloat("fractalSpeed", 0.01f)



        // Set the initial values of shape, colorScheme, branchWidth, and fractalSpeed
        fractalView.shape = shape ?: "Star"

        val colorSchemes = arrayOf(
            arrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA),
            arrayOf(Color.CYAN, Color.BLUE),
            // Add more color schemes here
            arrayOf(Color.RED, Color.YELLOW),
            arrayOf(Color.BLACK, Color.GRAY, Color.DKGRAY, Color.LTGRAY, Color.WHITE),
            )

        val colorSchemeNames = arrayOf("Rainbow", "Blue", "Red","Grayscale") // Names for the color schemes

        val spinner: Spinner = findViewById(R.id.colorSchemeSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorSchemeNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

// Default to "Rainbow"
        val defaultColorScheme = "Rainbow"
        val defaultIndex = colorSchemeNames.indexOf(defaultColorScheme)
        spinner.setSelection(defaultIndex)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                fractalView.setColorScheme(colorSchemes[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Get the screen height
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels

        // Set the height of the FractalView to be half of the screen height
        val layoutParams = fractalView.layoutParams
        layoutParams.height = screenHeight / 2
        fractalView.layoutParams = layoutParams

        val shapeSpinner: Spinner = findViewById(R.id.fractalShapeSpinner)
        val shapes = arrayOf("Star", "Square", "Triangle", "Pentagon") // Add more shapes as needed
        val shapeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, shapes)
        shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        shapeSpinner.adapter = shapeAdapter

        shapeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                fractalView.shape = shapes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        val branchWidthInput: EditText = findViewById(R.id.branchWidthInput)

        branchWidthInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val width: Float? = s.toString().toFloatOrNull()
                if (width != null && width in 1.0..10.0) {
                    fractalView.setBranchWidth(width)
                } else {
                    fractalView.setBranchWidth(8.0f)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        val fractalSpeedInput: EditText = findViewById(R.id.fractalSpeedInput)

        fractalSpeedInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val speed: Float? = s.toString().toFloatOrNull()
                if (speed != null && speed in 1.0..10.0) {
                    fractalView.setFractalSpeed(speed)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        val completeButton: Button = findViewById(R.id.complete_fractal_Button)
        completeButton.setOnClickListener {
            // Save the current values to SharedPreferences
            val sharedPreferences = getSharedPreferences("FractalArtPrefs", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putString("shape", fractalView.shape)
                putInt("colorScheme", spinner.selectedItemPosition)
                putFloat("branchWidth", branchWidthInput.text.toString().toFloatOrNull() ?: 8f)
                putFloat("fractalSpeed", fractalSpeedInput.text.toString().toFloatOrNull() ?: 0.01f)
                apply()
            }

            // Navigate back to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        spinner.setSelection(colorSchemePosition)


    }
}