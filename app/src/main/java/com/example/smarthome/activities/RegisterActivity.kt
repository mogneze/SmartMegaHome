package com.example.smarthome.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.R
import com.example.smarthome.User
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val textEmail: EditText = findViewById(R.id.regEmail)
        val textName: EditText = findViewById(R.id.regUsername)
        val textPassword: EditText = findViewById(R.id.regPassword)

        val btnRegister: Button = findViewById(R.id.regBtnRegister)
        btnRegister.setOnClickListener{
            lifecycleScope.launch {
                try {
                    supabaseClient.gotrue.signUpWith(Email) {
                        email = textEmail.text.toString()
                        password = textPassword.text.toString()
                    }
                    val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                    val userAdd = User(user.id, textName.text.toString(), "", "")
                    supabaseClient.postgrest["Users"].insert(userAdd)

                    startActivity(Intent(applicationContext, AddAddressActivity::class.java))
                    finish()
                }
                catch (e: Exception){
                    Log.e("!!!!", e.toString())
                }
            }
        }

        val btnLogIn: Button = findViewById(R.id.regBtnLogIn)
        btnLogIn.setOnClickListener{
            startActivity(Intent(applicationContext, LogInActivity::class.java))
            finish();
        }
    }
}