package com.example.smarthome.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.R
import com.example.smarthome.TestSingleton
import com.example.smarthome.TestSingleton.supabaseClient
import com.example.smarthome.TestSingleton.user
import io.github.jan.supabase.gotrue.LogoutScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import org.json.JSONArray


class ProfileActivity : AppCompatActivity() {
    companion object {
    const val SHARED_PREFS = "shared_prefs"
    const val EMAIL_KEY = "email_key"
    const val PASSWORD_KEY = "password_key"
}
    private lateinit var sharedpreferences: SharedPreferences
    private fun chooseImage(profileImage: ImageView){
        try {
            val selectImageIntent = registerForActivityResult(ActivityResultContracts.GetContent())
            { uri ->
                profileImage.setImageURI(uri)
            }
            selectImageIntent.launch("image/*")
        }catch (e: Exception){
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
            Log.e("erar", e.toString())
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val usernameET: EditText = findViewById(R.id.profileUsernameText)
        val emailET: EditText = findViewById(R.id.profileEmailText)
        val addressET: EditText = findViewById(R.id.profileAddressText)
        val profileImage: ImageView = findViewById(R.id.imgProfilePic)

        usernameET.setText(TestSingleton.username)
        addressET.setText(TestSingleton.userAddress)
        emailET.setText(user.email)
        lifecycleScope.launch {
            try{
                val n = supabaseClient.postgrest["Users"].select(columns = Columns.list("avatar")){
                    eq("id", user.id)
                }.body.toString()

                val array = JSONArray(n)
                val obj = array.getJSONObject(0)
                val avatar = obj.getString("avatar")

                val bucket = supabaseClient.storage["avatars"]
                val bytes: ByteArray = bucket.downloadPublic(avatar)
                val image: Drawable =
                    BitmapDrawable(resources, BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                profileImage.setImageDrawable(image)
            }catch (e: Exception){Log.e("Error loading avatar", e.toString()) }
        }
        profileImage.setOnClickListener{
            chooseImage(profileImage)
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
                    TestSingleton.username = usernameET.text.toString()
                    TestSingleton.userAddress = addressET.text.toString()
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

                    sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                    val editor = sharedpreferences.edit()
                    editor.clear()
                    editor.apply()

                    startActivity(intent)
                    TestSingleton.mainActivity.finish()
                    finish()
                }
                catch (e: Exception){
                    Log.e("error", e.toString())
                }
            }
        }
    }
}