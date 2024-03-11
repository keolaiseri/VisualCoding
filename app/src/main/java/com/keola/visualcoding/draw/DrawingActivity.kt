package com.keola.visualcoding.draw

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
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.HomeScreenActivity
import com.keola.visualcoding.R

class DrawingActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

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
        setContentView(R.layout.activity_drawing)

        drawingView = findViewById(R.id.drawingView)

        // Retrieve the saved values from SharedPreferences
        val sharedPreferences = getSharedPreferences("DrawingArtPrefs", Context.MODE_PRIVATE)
        val shape = sharedPreferences.getString("shape", drawingView.getBrushShape().name)
        val color = sharedPreferences.getString("color", "#000000")
        val size = sharedPreferences.getFloat("size", 5f)
        val opacity = sharedPreferences.getInt("opacity", 10)
        val shapeSpinnerPosition = sharedPreferences.getInt("shapeSpinnerPosition", 0)

        // Set the initial values of shape, color, size, and opacity
        drawingView.setBrushShape(BrushShape.valueOf(shape ?: "ROUND"))

        val colorInput = findViewById<EditText>(R.id.drawColorInput)
        colorInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val color = try {
                    Color.parseColor(s.toString())
                } catch (e: IllegalArgumentException) {
                    Color.BLACK // default to black if the entered hex code is invalid
                }
                drawingView.setPaintColor(color)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val strokeSizeInput = findViewById<EditText>(R.id.strokeSizeInput)
        strokeSizeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val size = try {
                    val inputSize = s.toString().toFloat()
                    if (inputSize in 1.0..10.0) inputSize * 3 else 25f
                } catch (e: NumberFormatException) {
                    25f // default size if the entered size is invalid
                }
                drawingView.setStrokeSize(size)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val drawingShapeSpinner = findViewById<Spinner>(R.id.drawingShapeSpinner)
        drawingShapeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedShape = BrushShape.values()[position]
                drawingView.setBrushShape(selectedShape)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        drawingView = findViewById(R.id.drawingView)
        drawingView.setOnLongClickListener {
            drawingView.clearCanvas()
            true
        }

        val opacityInput = findViewById<EditText>(R.id.opacityInput)
        opacityInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val opacity = try {
                    val inputOpacity = s.toString().toInt()
                    if (inputOpacity in 1..10) inputOpacity else 10
                } catch (e: NumberFormatException) {
                    10 // default opacity if the entered value is invalid
                }
                drawingView.setPaintOpacity(opacity)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val completeButton: Button = findViewById(R.id.complete_drawing_Button)
        completeButton.setOnClickListener {
            // Save the current values to SharedPreferences
            val sharedPreferences = getSharedPreferences("DrawingArtPrefs", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putString("shape", drawingView.getBrushShape().name)
                putString("color", colorInput.text.toString())
                putFloat("size", strokeSizeInput.text.toString().toFloatOrNull() ?: 5f)
                putInt("opacity", opacityInput.text.toString().toIntOrNull() ?: 10)
                putInt("shapeSpinnerPosition", drawingShapeSpinner.selectedItemPosition)
                apply()
            }

            // Navigate back to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        // Set the initial selected item of the spinner
        drawingShapeSpinner.setSelection(shapeSpinnerPosition)

    }


}