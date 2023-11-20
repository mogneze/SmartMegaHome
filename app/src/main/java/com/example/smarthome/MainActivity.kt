package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONArray

val roomsList: ArrayList<Room> = ArrayList()
val adapter = RoomsAdapter(roomsList)
val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kmmkqkhsgpvyyjurqstn.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImttbWtxa2hzZ3B2eXlqdXJxc3RuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA0NjkyNzIsImV4cCI6MjAxNjA0NTI3Mn0.MovxaxcIm0z1cR6xuWpwvHgk1Y5i-q5AEKBqkm_Q304"
) {
    install(GoTrue)
    install(Postgrest)
}
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler: RecyclerView = findViewById(R.id.recyclerRooms)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        roomsList.clear()
        lifecycleScope.launch {
            try{
                val rooms = supabaseClient.postgrest["Room"].select().body.toString()//.decodeSingle<Client>()

                val buf = StringBuilder()
                buf.append(rooms).append("\n");
                val root = JSONArray(buf.toString())

                for(i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getInt("id")
                    val name = obj.getString("name")
                    roomsList.add(Room(id, name))
                }
                adapter.notifyDataSetChanged()
            }
            catch (e: Exception){
                Log.e("!!!!!!!!!!!!", e.toString())
            }

        }
    }
}