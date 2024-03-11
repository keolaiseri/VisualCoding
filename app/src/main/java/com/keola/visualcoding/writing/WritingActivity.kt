package com.keola.visualcoding.writing

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.HomeScreenActivity
import com.keola.visualcoding.R
import java.util.LinkedList
import java.util.Queue

class WritingActivity: AppCompatActivity() {

    private var lastLength = 0

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
        setContentView(R.layout.activity_writing)


        val inputField = findViewById<EditText>(R.id.inputField)
        val writingView = findViewById<WritingView>(R.id.writingView)
        val writingColorInput = findViewById<EditText>(R.id.writingColorInput)

        // Retrieve the saved values from SharedPreferences
        val sharedPreferences = getSharedPreferences("WritingArtPrefs", Context.MODE_PRIVATE)
        val color = sharedPreferences.getString("color", "#000000")
        val size = sharedPreferences.getFloat("size", 100f)
        val fill = sharedPreferences.getString("fill", "True")
        val fillSpinnerPosition = sharedPreferences.getInt("fillSpinnerPosition", 0)
        //val inputFieldText = sharedPreferences.getString("inputFieldText", "") // Retrieve the saved text in the inputField


        inputField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()

                if (str.length > lastLength) {
                    // A letter was added
                    val newChar = str[lastLength]
                    lastLength = str.length

                    val handler = Handler(Looper.getMainLooper())
                    val runnable = object : Runnable {
                        override fun run() {
                            if (writingView.text == "Hello World") {
                                writingView.text = ""
                            }
                            writingView.text += newChar
                            writingView.animateText()
                        }
                    }
                    handler.postDelayed(runnable, 500) // delay of 500ms
                } else if (str.length < lastLength) {
                    // A letter was removed
                    lastLength = str.length
                    runOnUiThread {
                        writingView.text = str
                        writingView.invalidate()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        writingColorInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val colorString = s.toString()
                var color: Int? = null
                try {
                    // Try to parse the input as a hex color code
                    color = Color.parseColor(colorString)
                } catch (e: IllegalArgumentException) {
                    // If the input is not a valid hex color code, try to parse it as a color name
                    try {
                        val colorField = Color::class.java.getDeclaredField(colorString)
                        color = colorField.getInt(null)
                    } catch (e: Exception) {
                        // If the color name is not valid, return
                        return
                    }
                }
                if (color != null) {
                    // If the color is valid, set the color of the WritingView's paint
                    writingView.paint.color = color
                } else {
                    // If the color is invalid, set the color of the WritingView's paint to the default color black
                    writingView.paint.color = Color.BLACK
                }
                writingView.invalidate()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        val writingSizeInput = findViewById<EditText>(R.id.writingSizeInput)
        writingSizeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val sizeString = s.toString()
                val size: Float? = sizeString.toFloatOrNull()
                if (size == null || size !in 1.0..10.0) {
                    // If the size is invalid, set the text size of the WritingView's paint to the default size
                    writingView.paint.textSize = 100f
                } else {
                    // If the size is valid, set the text size of the WritingView's paint
                    writingView.paint.textSize = size * 15f
                }
                writingView.invalidate()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val fillSpinner = findViewById<Spinner>(R.id.fillSpinner)

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.fill_array, // This is an array in strings.xml containing "true" and "false"
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fillSpinner.adapter = adapter
        }

        fillSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "True") {
                    writingView.paint.style = Paint.Style.FILL
                } else if (selectedItem == "False") {
                    writingView.paint.style = Paint.Style.STROKE
                    writingView.paint.strokeWidth = 5f

                }
                writingView.invalidate()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        val completeButton: Button = findViewById(R.id.continue_writing_Button)
        completeButton.setOnClickListener {
            // Save the current values to SharedPreferences
            val sharedPreferences = getSharedPreferences("WritingArtPrefs", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putString("shape", writingView.paint.typeface.toString())
                putString("color", writingColorInput.text.toString())
                putFloat("size", writingSizeInput.text.toString().toFloatOrNull() ?: 100f)
                putString("fill", fillSpinner.selectedItem.toString())
                putInt("fillSpinnerPosition", fillSpinner.selectedItemPosition)
                apply()
            }

            // Navigate back to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        fillSpinner.setSelection(fillSpinnerPosition)

    }
}