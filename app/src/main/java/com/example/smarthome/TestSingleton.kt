package com.example.smarthome

import android.util.Log
import com.example.smarthome.activities.supabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object TestSingleton {
    fun TestSingleton(){
        GlobalScope.launch {
            val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
        }
    }
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