package com.example.smarthome.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.smarthome.R

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