package com.keola.visualcoding
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.keola.visualcoding.R.layout.activity_main
import com.keola.visualcoding.fireworks.FireworksActivity

class MainActivity : AppCompatActivity() {


    //Write code to inflate the activity_main.xml layout file
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        val continueButton: View = findViewById(R.id.continue_Button)
        continueButton.setOnClickListener {
            // Write code to start the FireworksActivity
            val intent = Intent(this, FireworksActivity::class.java)
            startActivity(intent)
        }

        // Check if FireworksFinishedActivity has been launched before
        val sharedPreferences = getSharedPreferences("FireworksPrefs", Context.MODE_PRIVATE)
        val hasFinished = sharedPreferences.getBoolean("hasFinished", false)

        if (hasFinished) {
            // If FireworksFinishedActivity has been launched before, redirect to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // If FireworksFinishedActivity has not been launched before, continue with MainActivity
            setContentView(R.layout.activity_main)
            // Rest of your code...
        }

    }



}