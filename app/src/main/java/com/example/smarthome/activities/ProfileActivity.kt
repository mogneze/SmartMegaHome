package com.example.smarthome.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.R
import io.github.jan.supabase.gotrue.LogoutScope
import io.github.jan.supabase.gotrue.gotrue
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnBack: ImageButton = findViewById(R.id.btnBackFromProfile)
        btnBack.setOnClickListener {
            finish();
        }
        val intent = Intent(this, LogInActivity::class.java)
        val btnExit: Button = findViewById(R.id.btnProfileExit)
        btnExit.setOnClickListener {
            lifecycleScope.launch {
                try {
                    supabaseClient.gotrue.logout()
                    startActivity(intent)
                    finish()
                }
                catch (e: Exception){
                    Log.e("error", e.toString())
                }
            }
        }
    }
}