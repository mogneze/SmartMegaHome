package com.example.smarthome.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R
import com.example.smarthome.Room
import com.example.smarthome.RoomAdd
import com.example.smarthome.adapters.RoomChAdapter
import com.example.smarthome.roomType
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AddRoomFragment : Fragment() {
    private val roomsList: ArrayList<Room> = ArrayList()
    lateinit var roomType: String
    private val adapter = RoomChAdapter(roomsList, object : RoomChAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {
            Toast.makeText(context, roomsList[position].type, Toast.LENGTH_SHORT).show()
            roomType=roomsList[position].type
            roomsList[position]
        }
    })
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomName: EditText = view.findViewById(R.id.textRoomName)
        val btnAddRoom: Button = view.findViewById(R.id.btnSaveRoom)
        btnAddRoom.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val user = com.example.smarthome.activities.supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                    val room = RoomAdd(user.id, roomName.text.toString(), roomType)
                    com.example.smarthome.activities.supabaseClient.postgrest["Rooms"].insert(room)

                    Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }catch (e: Exception){
                    Log.e("error", e.toString())}
            }
        }

        roomsList.add(Room(0, "Гостиная", "living_room"))
        roomsList.add(Room(1, "Кухня", "kitchen"))
        roomsList.add(Room(2, "Ванная", "bathroom"))
        roomsList.add(Room(3, "Кабинет", "office"))
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