package com.example.smarthome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R
import com.example.smarthome.Room

class RoomsAdapter(private val list: ArrayList<Room>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val roomImage: ImageView = itemView.findViewById(R.id.roomImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRoom: Room = list[position]
        holder.roomName.text = currentRoom.name
        when(currentRoom.type.lowercase()){
            "kitchen" -> holder.roomImage.setImageResource(R.drawable.kitchen_blue)
            "living_room" -> holder.roomImage.setImageResource(R.drawable.living_room_blue)
            "bathroom" -> holder.roomImage.setImageResource(R.drawable.bathroom_blue)
            "bedroom" -> holder.roomImage.setImageResource(R.drawable.bedroom_blue)
            "office" -> holder.roomImage.setImageResource(R.drawable.office_blue)
            "hall" -> holder.roomImage.setImageResource(R.drawable.hall_blue)
        }
        holder.itemView.setOnClickListener(){
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = list.size
}