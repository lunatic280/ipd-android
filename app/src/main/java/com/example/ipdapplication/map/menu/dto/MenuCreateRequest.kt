package com.example.ipdapplication.map.menu.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuCreateRequest(
    val placeId: Long,
    val name: String,
    val price: Int,
    val description: String,
    val imageUrl: String = ""
)
