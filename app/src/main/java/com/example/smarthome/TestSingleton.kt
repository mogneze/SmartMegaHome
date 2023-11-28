package com.example.smarthome

import android.util.Log
import com.example.smarthome.activities.MainActivity
import com.example.smarthome.activities.supabaseClient
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object TestSingleton {
    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://kmmkqkhsgpvyyjurqstn.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImttbWtxa2hzZ3B2eXlqdXJxc3RuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA0NjkyNzIsImV4cCI6MjAxNjA0NTI3Mn0.MovxaxcIm0z1cR6xuWpwvHgk1Y5i-q5AEKBqkm_Q304"
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Storage)
    }
    fun getClient(): SupabaseClient {
        return supabaseClient
    }
    fun TestSingleton(){
        GlobalScope.launch {
            val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
        }
    }
    lateinit var mainActivity: MainActivity
    var username: String = ""
    var userAddress: String = ""
    fun updateDeviceState(deviceId: Int, isTurned: Boolean){
        GlobalScope.launch {
            try{
                supabaseClient.postgrest["Devices"].update({
                    set("isTurnedOn", isTurned)
                }){ eq("id", deviceId)}
            }
            catch (e: Exception){
                Log.e("ашипк", e.toString())
            }
        }
    }
    fun updateDevice(deviceId: Int, isTurned: Boolean, param1: Int, param2: Int){
        GlobalScope.launch {
            try {
                supabaseClient.postgrest["Devices"].update({
                    set("isTurnedOn", isTurned)
                    set("param", param1)
                    set("param2", param2)
                }){ eq("id", deviceId)}
            }
            catch (e: Exception){
                Log.e("шмяк", e.toString())
            }
        }
    }
}