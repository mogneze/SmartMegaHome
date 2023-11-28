package com.example.smarthome

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String, val name: String, val address: String, val avatar: String)