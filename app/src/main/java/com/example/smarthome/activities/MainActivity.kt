package com.example.smarthome.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.smarthome.R
import com.example.smarthome.Room
import com.example.smarthome.adapters.RoomsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import org.json.JSONArray

private lateinit var refresher: SwipeRefreshLayout
val roomsList: ArrayList<Room> = ArrayList()
val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kmmkqkhsgpvyyjurqstn.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImttbWtxa2hzZ3B2eXlqdXJxc3RuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA0NjkyNzIsImV4cCI6MjAxNjA0NTI3Mn0.MovxaxcIm0z1cR6xuWpwvHgk1Y5i-q5AEKBqkm_Q304"
) {
    install(GoTrue)
    install(Postgrest)
}
class MainActivity : AppCompatActivity() {
    val act: Activity = this
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

        loadData()
        val act: Activity = this;

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

        refresher= findViewById(R.id.roomsRefresher)
        refresher.setOnRefreshListener {
            loadData()
        }
    }
    private fun loadData(){
        //roomsList.clear()
        lifecycleScope.launch {
            try{
                val addressText: TextView = findViewById(R.id.textAddress)
                val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)

                val n = supabaseClient.postgrest["Users"].select(columns = Columns.list("address")){
                    eq("id", user.id)
                }.body.toString()

                val array = JSONArray(n)
                val obj = array.getJSONObject(0)
                val address = obj.getString("address")
                addressText.text = address

                val rooms = supabaseClient.postgrest["Rooms"].select(){
                    eq("user_id", user.id)
                }.body.toString()//.decodeSingle<Client>()

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
                adapter.notifyDataSetChanged()
                refresher.isRefreshing = false
            }
            catch (e: Exception){
                Log.e("!!!!!!!!!!!!", e.toString())
            }
        }
    }
}