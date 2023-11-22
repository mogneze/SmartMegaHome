package com.example.smarthome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R
import com.example.smarthome.Room

class RoomChAdapter (private val list: ArrayList<Room>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RoomChAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomImage: ImageButton = itemView.findViewById(R.id.roomImageCh)
        val roomName: TextView = itemView.findViewById(R.id.textRoomNameCh)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.choose_room_item, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRoom: Room = list[position]
        holder.roomName.text = currentRoom.name
        when(currentRoom.type.lowercase()){
            "living_room" -> holder.roomImage.setImageResource(R.drawable.livingroom_white)
            "kitchen" -> holder.roomImage.setImageResource(R.drawable.kitchen_white)
            "bathroom" -> holder.roomImage.setImageResource(R.drawable.bathroom_white)
            "bedroom" -> holder.roomImage.setImageResource(R.drawable.bedroom_white)
            "office" -> holder.roomImage.setImageResource(R.drawable.office_white)
            "hall" -> holder.roomImage.setImageResource(R.drawable.hall_white)
        }
        holder.itemView.setOnClickListener(){
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = list.size
}