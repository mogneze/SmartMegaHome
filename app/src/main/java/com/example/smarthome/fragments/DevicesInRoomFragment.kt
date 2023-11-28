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
import com.example.smarthome.TestSingleton
import com.example.smarthome.TestSingleton.supabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONArray

val deviceList: ArrayList<Device> = ArrayList()
class DevicesInRoomFragment : Fragment() {
    private val adapter = DevicesAdapter(deviceList, object : DevicesAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {
            val bundle = Bundle()
            bundle.putString("page", "device")
            bundle.putInt("deviceId", deviceList[position].id)
            bundle.putString("deviceName", deviceList[position].name)
            bundle.putString("deviceType", deviceList[position].type)
            bundle.putString("deviceParam1", deviceList[position].param1)
            bundle.putString("deviceParam2", deviceList[position].param2)
            bundle.putBoolean("deviceTurned", deviceList[position].isTurnedOn)
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    })
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
        TestSingleton.DevicesInRoomFragment = this
        loadDevices()

        return inflater.inflate(R.layout.fragment_devices_in_room, container, false)
    }
    fun loadDevices(){
        deviceList.clear()
        val bundle = arguments
        val roomId = bundle!!.getInt("roomId")
        lifecycleScope.launch {
            try{
                val devices = supabaseClient.postgrest["Devices"].select{
                    eq("room_id", roomId)
                }.body.toString()

                val buf = StringBuilder()
                buf.append(devices).append("\n");
                val root = JSONArray(buf.toString())

                for(i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getInt("id")
                    //roomId чето да
                    val name = obj.getString("name")
                    val type = obj.getString("type")
                    val isTurned = obj.getBoolean("isTurnedOn")
                    val param1 = obj.getString("param")
                    val param2 = obj.getString("param2")
                    val identifier = obj.getString("identifier")
                    deviceList.add(Device(id, roomId, name, type, isTurned, param1, param2, identifier))
                }
                adapter.notifyDataSetChanged()
            }
            catch (e: Exception){
                Log.e("DeviceList loading error", e.toString())
            }
        }
    }
}