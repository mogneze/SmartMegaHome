package com.example.smarthome

import kotlinx.serialization.Serializable

@Serializable
data class Device(val id: Int, val roomId: Int, val name: String, val type: String, var isTurnedOn: Boolean, val param1: String, val param2: String, val identifier: String)
