package com.example.smarthome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomsAdapter(private val list: ArrayList<Room>) : RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.roomName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: RoomsAdapter.ViewHolder, position: Int) {
        val currentRoom: Room = list[position]
        holder.roomName.text = currentRoom.name
    }
    override fun getItemCount() = list.size
}