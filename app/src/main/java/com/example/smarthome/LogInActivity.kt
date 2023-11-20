package com.example.smarthome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val frContainer: FragmentContainerView = findViewById(R.id.fragmentContainerView)
        val frTransaction: FragmentTransaction

        val tr = supportFragmentManager.beginTransaction()
        tr.replace(R.id.fragmentContainerView, LogInFragment())
        tr.commit()

        val btnLogIn: Button = findViewById(R.id.btnLogIn)
        btnLogIn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}