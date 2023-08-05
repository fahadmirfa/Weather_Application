package com.mirfa.weather_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)
        // Set up a timer to navigate to the main activity after 3 seconds
        val splashTimeOut = 3000L // 3 seconds
        val mainIntent = Intent(this, MainActivity::class.java)

        // Launch the main activity after the timer expires
        // We use a lambda expression here to avoid creating an unnecessary anonymous class
        // The lambda will be executed after the specified delay (splashTimeOut)
        // It starts the main activity and finishes the splash activity
        // So that the user cannot go back to the splash screen by pressing the back button
        android.os.Handler().postDelayed({
            startActivity(mainIntent)
            finish()
        }, splashTimeOut)

    }
}