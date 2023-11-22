package com.example.smarthome.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R
import com.example.smarthome.Room
import com.example.smarthome.adapters.RoomChAdapter

class AddRoomFragment : Fragment() {
    private val roomsList: ArrayList<Room> = ArrayList()
    private val adapter = RoomChAdapter(roomsList, object : RoomChAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {

        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomsList.add(Room(0, "Гостиная", "living_room"))
        roomsList.add(Room(1, "Кухня", "kitchen"))
        roomsList.add(Room(2, "Ванная", "bathroom"))
        roomsList.add(Room(3, "Кабинет", "cabinet"))
        roomsList.add(Room(4, "Спальня", "bedroom"))
        roomsList.add(Room(5, "Зал", "hall"))

        val recycler: RecyclerView = view.findViewById(R.id.chooseRoomRecycle)
        recycler.layoutManager = GridLayoutManager(context, 3)
        recycler.adapter = adapter
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_room, container, false)
    }
}