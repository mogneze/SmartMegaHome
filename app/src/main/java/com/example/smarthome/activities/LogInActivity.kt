package com.example.smarthome.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.R
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.LogoutScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

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
                        val editor = sharedpreferences.edit()
                        editor.putString(EMAIL_KEY, textEmail.text.toString())
                        editor.putString(PASSWORD_KEY, textPassword.text.toString())
                        editor.apply()

                        startActivity(intent)
                        finish();
                    } catch (e: BadRequestRestException){
                        Toast.makeText(applicationContext, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                    catch (e: Exception) {
                        Log.e("!!!!", e.toString())
                    }
                }
            }
            else Toast.makeText(applicationContext, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
        val btnRegister: Button = findViewById(R.id.logBtnRegister)
        btnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish();
        }
    }
}