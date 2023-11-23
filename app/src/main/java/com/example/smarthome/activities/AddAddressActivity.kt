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
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AddAddressActivity : AppCompatActivity() {
    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://kmmkqkhsgpvyyjurqstn.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImttbWtxa2hzZ3B2eXlqdXJxc3RuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA0NjkyNzIsImV4cCI6MjAxNjA0NTI3Mn0.MovxaxcIm0z1cR6xuWpwvHgk1Y5i-q5AEKBqkm_Q304"
    ) {
        install(GoTrue)
        install(Postgrest)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        val txtAddress: EditText = findViewById(R.id.textAddressSave)
        val btnSave: Button = findViewById(R.id.btnSaveAddress)
        btnSave.setOnClickListener {
            if(txtAddress.text.toString() != ""){
                val intent = Intent(applicationContext, MainActivity::class.java)
                lifecycleScope.launch {
                    try {
                        val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                        supabaseClient.postgrest["Users"].update({
                            set("address", txtAddress.text.toString())
                        }){
                            eq("id", user.id)
                        }
                        startActivity(intent)
                    }catch (e: Exception){Log.e("error", e.toString())}
                }
            }
        }
    }
}