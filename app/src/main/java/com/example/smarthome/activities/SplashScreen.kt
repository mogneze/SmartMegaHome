package com.example.smarthome.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.R
import com.example.smarthome.TestSingleton
import com.example.smarthome.TestSingleton.supabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    companion object {
    const val SHARED_PREFS = "shared_prefs"
    const val EMAIL_KEY = "email_key"
    const val PASSWORD_KEY = "password_key"
}
    private lateinit var sharedpreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        lifecycleScope.launch {
            try {
                supabaseClient.gotrue.loginWith(Email) {
                    email = sharedpreferences.getString(EMAIL_KEY, null).toString()
                    password = sharedpreferences.getString(PASSWORD_KEY, null).toString()
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish();
            }
            catch (e: Exception) {
                Log.e("!!!!", e.toString())
                startActivity(Intent(applicationContext, LogInActivity::class.java))
            }
        }
    }
}