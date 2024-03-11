package com.keola.visualcoding.fireworks

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.keola.visualcoding.HomeScreenActivity
import com.keola.visualcoding.R

import java.util.Random

class FireworksActivity : AppCompatActivity() {


    private lateinit var fireworksView: FireworksView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fireworks)

        fireworksView = findViewById(R.id.fireworksSurfaceView)


        val continueButtonSize: Button = findViewById(R.id.continue_fireworks_Button)
        continueButtonSize.isEnabled = false // Disable the button until a size is entered

        val colorSpinner: Spinner = findViewById(R.id.colorSpinner)

        val colorOptions = arrayOf("Select color", "Blue", "Red", "Green", "Orange", "Yellow", "Purple") // Add more colors as needed

        val adapter = ArrayAdapter(this, R.layout.spinner_item, colorOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = adapter

        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    // Ignore the default item
                    return
                }

                val selectedColor = when (position) {
                    0 -> getRandomColor() // Get a random color
                    1 -> ContextCompat.getColor(this@FireworksActivity, R.color.colorBlue)
                    2 -> ContextCompat.getColor(this@FireworksActivity, R.color.colorRed)
                    3 -> ContextCompat.getColor(this@FireworksActivity, R.color.colorGreen)
                    4 -> ContextCompat.getColor(this@FireworksActivity, R.color.colorOrange)
                    5 -> ContextCompat.getColor(this@FireworksActivity, R.color.colorYellow)
                    6 -> ContextCompat.getColor(this@FireworksActivity, R.color.colorPurple)
                    else -> getRandomColor()
                }

                setFireworksColor(selectedColor)
                continueButtonSize.isEnabled = true //enable the button

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Show the hint

            }

            fun getRandomColor(): Int {
                val rnd = Random()
                return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            }
        }


        continueButtonSize.setOnClickListener {
            if (continueButtonSize.isEnabled) {
                // Write code to start the FireworksSize activity
                val intent = Intent(this, FireworksSize::class.java)
                intent.putExtra("selectedColor", fireworksView.selectedColor)
                startActivity(intent)
            } else {
                Toast.makeText(this@FireworksActivity, "Please select a color before proceeding", Toast.LENGTH_SHORT).show()
            }
        }


        // Touch listener for triggering fireworks
        findViewById<View>(R.id.fireworksSurfaceView).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y
                    fireworksView.addFirework(x, y)
                    return@setOnTouchListener true
                }
                else -> false
            }
        }
    }


    private fun setFireworksColor(color: Int) {
        fireworksView.selectedColor = color
    }


    // Handle the click action if needed
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.fireworks_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_choose_color -> showColorPopupMenu(findViewById<View>(R.id.action_choose_color))
            // Handle other menu items if needed
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showColorPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(this, anchorView)
        popupMenu.inflate(R.menu.color_menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.color_red -> setFireworksColor(ContextCompat.getColor(this, colorRed))
                R.id.color_blue -> setFireworksColor(ContextCompat.getColor(this, colorBlue))
                // Add more colors as needed
            }
            true
        }

        popupMenu.show()
    }*/



}
