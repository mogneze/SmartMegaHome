package com.example.smarthome.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.R
import io.github.jan.supabase.gotrue.LogoutScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import org.json.JSONArray


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val usernameET: EditText = findViewById(R.id.profileUsernameText)
        val emailET: EditText = findViewById(R.id.profileEmailText)
        val addressET: EditText = findViewById(R.id.profileAddressText)
        lifecycleScope.launch {
            try {
                val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)

                val n = supabaseClient.postgrest["Users"].select(columns = Columns.list("name","address")) {
                    eq("id", user.id)
                }.body.toString()
                var array = JSONArray(n)
                var obj = array.getJSONObject(0)
                val username = obj.getString("name")
                val address = obj.getString("address")
                usernameET.setText(username)
                addressET.setText(address)
                emailET.setText(user.email)
            }catch (e: Exception){Log.e("error", e.toString())}
        }

        val btnBack: ImageButton = findViewById(R.id.btnBackFromProfile)
        btnBack.setOnClickListener {
            finish();
        }

        val btnSave: Button = findViewById(R.id.btnProfileSave)
        btnSave.setOnClickListener {
            lifecycleScope.launch {
                try {
                    var user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                    supabaseClient.postgrest["Users"].update({
                        set("address", addressET.text.toString())
                        set("name", usernameET.text.toString())
                    }) {
                        eq("id", user.id)
                    }
                    user = supabaseClient.gotrue.modifyUser {
                        email = emailET.text.toString()
                    }
                    Toast.makeText(applicationContext, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                }catch (e: Exception){Log.e("error", e.toString())}
            }
        }

        val intent = Intent(this, LogInActivity::class.java)
        val btnExit: Button = findViewById(R.id.btnProfileExit)
        btnExit.setOnClickListener {
            lifecycleScope.launch {
                try {
                    supabaseClient.gotrue.logout(scope = LogoutScope.GLOBAL)
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