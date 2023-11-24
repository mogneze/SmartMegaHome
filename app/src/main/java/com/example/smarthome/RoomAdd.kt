package com.example.smarthome

import kotlinx.serialization.Serializable

@Serializable
data class RoomAdd (val user_id: String, val name: String, val type: String)