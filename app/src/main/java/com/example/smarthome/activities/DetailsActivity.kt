package com.example.smarthome.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.smarthome.R
import com.example.smarthome.fragments.AddRoomFragment
import com.example.smarthome.fragments.DeviceSettingsFragment
import com.example.smarthome.fragments.DevicesInRoomFragment

class DetailsActivity : AppCompatActivity() {
    //private val pageManager = PageManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val bundle = intent.extras
        var page: String? = null
        page = bundle!!.getString("page", "default")
        val roomName = bundle.getString("roomName", "default")

        //val roomId = intent.getIntExtra("roomId", 0)
        val pageName: TextView = findViewById(R.id.textPageTitle)
        when (page) {
            "addRoom" -> {
                pageName.text = "Добавить комнату"
                replaceFragment(AddRoomFragment())
                //replaceFragment(AddRoomFragment())
                //val bundle = Bundle()
                //findNavController(R.id.nav_graph).navigate(R.id.addRoomFragment, bundle)
            }
            "devicesInRoom" -> {
                val fr = DevicesInRoomFragment()
                fr.arguments = bundle
                pageName.text = "Устройства в $roomName"
                replaceFragment(fr)
            }
            "addDevice" -> {
                pageName.text = "Добавить устройство"
                //replaceFragment(DevicesInRoomFragment())
            }
            "device" -> {
                val fr = DeviceSettingsFragment()
                fr.arguments = bundle
                pageName.text = "Устройство"
                replaceFragment(fr)
            }
        }

        val btnBack: ImageButton = findViewById(R.id.btnBackFromDetails)
        btnBack.setOnClickListener {
            finish();
        }
    }
    fun replaceFragment(fragment: Fragment){
        val frManager = supportFragmentManager
        val frTransaction = frManager.beginTransaction()
        frTransaction.replace(R.id.pageContainer, fragment)
        frTransaction.commit()
    }
}/*
object PageManager : AppCompatActivity(){
    fun replaceFragment(fragment: Fragment){
        val frManager = supportFragmentManager
        val frTransaction = frManager.beginTransaction()
        frTransaction.replace(R.id.pageContainer, fragment)
        frTransaction.commit()
    }
}*/