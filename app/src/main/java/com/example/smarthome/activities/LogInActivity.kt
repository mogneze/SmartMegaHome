package com.example.smarthome.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.R
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://kmmkqkhsgpvyyjurqstn.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImttbWtxa2hzZ3B2eXlqdXJxc3RuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA0NjkyNzIsImV4cCI6MjAxNjA0NTI3Mn0.MovxaxcIm0z1cR6xuWpwvHgk1Y5i-q5AEKBqkm_Q304"
    ) {
        install(GoTrue)
        install(Postgrest)
    }
    val goTrue = supabaseClient.gotrue;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val textEmail: EditText = findViewById(R.id.logEmail)
        val textPassword: EditText = findViewById(R.id.logPassword)

        val btnLogIn: Button = findViewById(R.id.logBtnLogIn)
        btnLogIn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            if(textEmail.text.toString() != "" && textPassword.text.toString() != "") {
                lifecycleScope.launch {
                    try {
                        supabaseClient.gotrue.loginWith(Email) {
                            email = textEmail.text.toString()
                            password = textPassword.text.toString()
                        }
                        startActivity(intent)
                        finish();
                    } catch (e: Exception) {
                        Log.e("!!!!", e.toString())

                    }
                }
            }
        }
        val btnRegister: Button = findViewById(R.id.logBtnRegister)
        btnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish();
        }
    }
}