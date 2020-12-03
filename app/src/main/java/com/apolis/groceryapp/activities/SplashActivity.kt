package com.apolis.groceryapp.activities

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import com.apolis.groceryapp.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // handler allows communicating back with UI thread from other background thread
        Handler().postDelayed(Runnable {
            // this method will be executed when the timer is over
            chooseStartScreen()
            finish()
        }, SPLASH_DISPLAY_LENGTH)
    }

    private fun chooseStartScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}