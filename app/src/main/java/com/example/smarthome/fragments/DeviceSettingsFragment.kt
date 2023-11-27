package com.example.smarthome.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import com.example.smarthome.Device
import com.example.smarthome.R
import com.example.smarthome.TestSingleton
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class DeviceSettingsFragment : Fragment() {
    private lateinit var device: Device
    private lateinit var deviceSwitch: SwitchCompat
    private lateinit var deviceParam1Bar: SeekBar
    private lateinit var deviceParam2Bar: SeekBar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceNameText: TextView = view.findViewById(R.id.deviceSettingsName)
        val deviceParamName1: TextView = view.findViewById(R.id.deviceSettingsParam1)
        val deviceParamName2: TextView = view.findViewById(R.id.deviceSettingsParam2)
        deviceParam1Bar = view.findViewById(R.id.settingsParam1Bar)
        deviceParam2Bar = view.findViewById(R.id.settingsParam2Bar)
        val deviceImage: ImageView = view.findViewById(R.id.deviceSettingsImage)
        deviceSwitch = view.findViewById(R.id.deviceSettingsSwitch)
        lateinit var param1Name: String; lateinit var param2Name: String

        deviceParamName2.visibility = View.GONE
        deviceParam2Bar.visibility = View.GONE

        deviceNameText.text = device.name
        deviceParam1Bar.progress = device.param1.toInt()
        deviceSwitch.isChecked = device.isTurnedOn
        when(device.type){
            "light" ->{
                param1Name = " яркость"
                deviceImage.setImageResource(R.drawable.bulb_blue)
            }
            "conditioner" ->{
                deviceParamName2.visibility = View.VISIBLE
                deviceParam2Bar.visibility = View.VISIBLE
                param1Name = "°C градусов"
                param2Name = "% мощность"
                deviceParam1Bar.max = 35
                deviceParam2Bar.progress = device.param2.toInt()
                deviceImage.setImageResource(R.drawable.condi_blue)

                deviceParamName2.text = "${device.param2}$param2Name"
                deviceParam2Bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        deviceParamName2.text = deviceParam2Bar.progress.toString() + param2Name
                    }
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                })
            }
            "fan" ->{
                param1Name = "% мощность"
                deviceImage.setImageResource(R.drawable.fan_blue)
            }
            "temperature" ->{
                param1Name = "°C градусов"
                deviceParam1Bar.max = 35
                deviceImage.setImageResource(R.drawable.thermo_blue)
            }
            "hood" ->{
                param1Name = "% мощность"
                deviceImage.setImageResource(R.drawable.hood_blue)
            }
        }
        deviceParamName1.text = "${device.param1}$param1Name"
        deviceParam1Bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                deviceParamName1.text = deviceParam1Bar.progress.toString() + param1Name
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        val deviceName = bundle!!.getString("deviceName").toString()
        val deviceId = bundle.getInt("deviceId")
        val deviceType = bundle.getString("deviceType").toString()
        val deviceTurned = bundle.getBoolean("deviceTurned")
        val deviceParam1 = bundle.getString("deviceParam1").toString()
        val deviceParam2 = bundle.getString("deviceParam2").toString()
        device = Device(deviceId, 0, deviceName, deviceType, deviceTurned, deviceParam1, deviceParam2)
        lifecycleScope.launch {
            try{
            }catch (e: Exception) { Log.e("error", e.toString())}

        }
        return inflater.inflate(R.layout.fragment_device_settings, container, false)
    }
    private fun updateDevice(){
        lifecycleScope.launch {
            try {
                com.example.smarthome.activities.supabaseClient.postgrest["Devices"].update({
                    set("isTurnedOn", deviceSwitch.isChecked)
                    set("param", deviceParam1Bar.progress)
                    set("param2", deviceParam2Bar.progress)
                }){
                    eq("id", device.id)
                }
            }catch (e: Exception){Log.e("шмяк", e.toString())}
        }
    }
    override fun onPause() {
        super.onPause()
        //updateDevice()
        TestSingleton.updateDevice(device.id, deviceSwitch.isChecked, deviceParam1Bar.progress, deviceParam2Bar.progress)
    }
}