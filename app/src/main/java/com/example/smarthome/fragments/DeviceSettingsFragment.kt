package com.example.smarthome.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R
import io.github.jan.supabase.gotrue.gotrue
import kotlinx.coroutines.launch

class DeviceSettingsFragment : Fragment() {
    private lateinit var deviceName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceNameText: TextView = view.findViewById(R.id.deviceSettingsName)
        deviceNameText.text = deviceName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        deviceName = bundle!!.getString("deviceName").toString()
        lifecycleScope.launch {
            try{
            }catch (e: Exception) { Log.e("error", e.toString())}

        }
        return inflater.inflate(R.layout.fragment_device_settings, container, false)
    }

}