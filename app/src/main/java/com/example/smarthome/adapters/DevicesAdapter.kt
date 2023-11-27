package com.example.smarthome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.Device
import com.example.smarthome.R

class DevicesAdapter(private val list: ArrayList<Device>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.deviceNameText)
        val deviceParam1: TextView = itemView.findViewById(R.id.deviceParam1Text)
        val deviceParam2: TextView = itemView.findViewById(R.id.deviceParam2Text)
        val deviceSwitch: SwitchCompat = itemView.findViewById(R.id.deviceSwitch)
        val deviceImage: ImageView = itemView.findViewById(R.id.deviceImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDevice: Device = list[position]
        holder.deviceName.text = currentDevice.name
        holder.deviceParam1.text = currentDevice.param1
        holder.deviceParam2.text = currentDevice.param2
        holder.deviceSwitch.isChecked = currentDevice.isTurnedOn
        when(currentDevice.type){
            "light" -> holder.deviceImage.setImageResource(R.drawable.bulb_blue)
            "fan" -> holder.deviceImage.setImageResource(R.drawable.fan_blue)
            "hood" -> holder.deviceImage.setImageResource(R.drawable.hood_blue)
            "temperature" -> holder.deviceImage.setImageResource(R.drawable.thermo_blue)
            "conditioner" -> holder.deviceImage.setImageResource(R.drawable.condi_blue)
        }
        holder.itemView.setOnClickListener(){
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = list.size
}