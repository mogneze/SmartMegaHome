package com.example.smarthome

import kotlinx.serialization.Serializable

@Serializable
data class Device(val id: Int, val roomId: Int, val name: String, val type: String, val isTurnedOn: Boolean, val param1: String, val param2: String)
