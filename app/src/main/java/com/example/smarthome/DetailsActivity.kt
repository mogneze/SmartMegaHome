package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.smarthome.fragments.AddRoomFragment
import com.example.smarthome.fragments.DevicesInRoomFragment

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val bundle = intent.extras
        var page: String? = null
        page = bundle!!.getString("page", "default")
        val pageName: TextView = findViewById(R.id.textPageTitle)
        if(page == "addRoom"){
            pageName.text = "Добавить комнату"
            replaceFragment(AddRoomFragment())
        }else if (page == "devicesInRoom"){
            pageName.text = "Устройства в ..."
            replaceFragment(DevicesInRoomFragment())
        }

        val btnBack: ImageButton = findViewById(R.id.btnBackFromDetails)
        btnBack.setOnClickListener {
            finish();
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val frManager = supportFragmentManager
        val frTransaction = frManager.beginTransaction()
        frTransaction.replace(R.id.pageContainer, fragment)
        frTransaction.commit()
    }
}