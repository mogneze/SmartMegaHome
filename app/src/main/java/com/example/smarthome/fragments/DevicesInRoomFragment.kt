package com.example.smarthome.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.activities.DetailsActivity
import com.example.smarthome.Device
import com.example.smarthome.adapters.DevicesAdapter
import com.example.smarthome.R
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONArray

val deviceList: ArrayList<Device> = ArrayList()
val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kmmkqkhsgpvyyjurqstn.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImttbWtxa2hzZ3B2eXlqdXJxc3RuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA0NjkyNzIsImV4cCI6MjAxNjA0NTI3Mn0.MovxaxcIm0z1cR6xuWpwvHgk1Y5i-q5AEKBqkm_Q304"
) {
    install(GoTrue)
    install(Postgrest)
}
class DevicesInRoomFragment : Fragment() {
    private val adapter = DevicesAdapter(deviceList, object : DevicesAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {
            val bundle = Bundle()
            bundle.putString("page", "device")
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            //DetailsActivity().replaceFragment(AddRoomFragment())
            //val bundle = Bundle()
            //findNavController(R.id.nav_graph).navigate(R.id.deviceFragment, bundle)
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler: RecyclerView = view.findViewById(R.id.devicesRecycle)
        recycler.layoutManager = GridLayoutManager(context, 2)
        recycler.adapter = adapter
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        deviceList.clear()
        lifecycleScope.launch {
            try{
                val devices = supabaseClient.postgrest["Devices"].select().body.toString()//.decodeSingle<Client>()

                val buf = StringBuilder()
                buf.append(devices).append("\n");
                val root = JSONArray(buf.toString())

                for(i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getInt("id")
                    val roomId = obj.getInt("room_id")
                    val name = obj.getString("name")
                    val type = obj.getString("type")
                    //val isTurned = obj.getBoolean("isTurnedOn")
/*                    val param1 = obj.getInt("param")
                    val param2 = obj.getInt("param2")*/
                    deviceList.add(Device(id, roomId, name, type, true, 1, 2))
                }
                adapter.notifyDataSetChanged()
            }
            catch (e: Exception){
                Log.e("!!!!!!!!!!!!", e.toString())
            }
        }
        return inflater.inflate(R.layout.fragment_devices_in_room, container, false)

    }
}