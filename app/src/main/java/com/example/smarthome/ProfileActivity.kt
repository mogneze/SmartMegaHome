package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnExit: ImageButton = findViewById(R.id.btnBackFromProfile)
        btnExit.setOnClickListener {
            finish();
        }
    }
}