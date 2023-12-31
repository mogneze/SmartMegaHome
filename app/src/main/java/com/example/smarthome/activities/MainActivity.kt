package com.example.smarthome.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.smarthome.R
import com.example.smarthome.Room
import com.example.smarthome.TestSingleton
import com.example.smarthome.TestSingleton.supabaseClient
import com.example.smarthome.TestSingleton.user
import com.example.smarthome.adapters.RoomsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import org.json.JSONArray

val roomsList: ArrayList<Room> = ArrayList()

class MainActivity : AppCompatActivity() {
    private lateinit var refresher: SwipeRefreshLayout
    private val adapter = RoomsAdapter(roomsList, object : RoomsAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {
            val bundle = Bundle()
            bundle.putString("page", "devicesInRoom")
            bundle.putInt("roomId", roomsList[position].id)
            bundle.putString("roomName", roomsList[position].name)
            intent = Intent(applicationContext, DetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TestSingleton.mainActivity = this

        val btnProfile: ImageButton = findViewById(R.id.btnSettings)
        btnProfile.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        val btnAddRoom: FloatingActionButton = findViewById(R.id.btnAddRoom)
        btnAddRoom.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("page", "addRoom")
            intent = Intent(this, DetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        val recycler: RecyclerView = findViewById(R.id.recyclerRooms)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        refresher = findViewById(R.id.roomsRefresher)
        refresher.setOnRefreshListener{ loadData() }
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    private fun loadData(){
        lifecycleScope.launch {
            try{
                //TestSingleton.getUserData()
                //roomsList.clear()
                val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                TestSingleton.user = user

                val n = supabaseClient.postgrest["Users"].select(columns = Columns.list("address", "name")){
                    eq("id", user.id)
                }.body.toString()

                val array = JSONArray(n)
                val obj = array.getJSONObject(0)
                val address = obj.getString("address")
                val name = obj.getString("name")
                TestSingleton.userAddress = address
                TestSingleton.username = name
                val addressText: TextView = findViewById(R.id.textAddress)
                if(address == "") addressText.text = "Адрес не указан"
                else addressText.text = address

                val rooms = supabaseClient.postgrest["Rooms"].select(){
                    eq("user_id", user.id)
                }.body.toString()

                val buf = StringBuilder()
                buf.append(rooms).append("\n");
                val root = JSONArray(buf.toString())

                for(i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getInt("id")
                    val name = obj.getString("name")
                    val type = obj.getString("type")
                    val room = Room(id, name, type)
                    if(!roomsList.contains(room)) roomsList.add(room)
                }
                val txtLoading: TextView = findViewById(R.id.textLoading)
                if(roomsList.isNotEmpty()) txtLoading.text = ""
                else txtLoading.text = "У вас пока нет комнат.\nНажмите \"+\" чтобы добавить."
                adapter.notifyDataSetChanged()
                refresher.isRefreshing = false
            }
            catch (e: Exception){
                Toast.makeText(applicationContext, "Произошла ошибка при загрузке", Toast.LENGTH_SHORT).show()
                Log.e("Error loading main activity", e.toString())
            }
        }
//        lifecycleScope.launch {
//            try {
//                val n = supabaseClient.postgrest["Users"].select(columns = Columns.list("name","address")) {
//                    eq("id", user.id)
//                }.body.toString()
//                var array = JSONArray(n)
//                var obj = array.getJSONObject(0)
//                TestSingleton.username = obj.getString("name")
//                TestSingleton.userAddress = obj.getString("address")
//            }catch (e: Exception){Log.e("error", e.toString())}
//        }
    }
}