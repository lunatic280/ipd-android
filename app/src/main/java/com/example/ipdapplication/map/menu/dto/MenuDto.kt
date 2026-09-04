package com.example.ipdapplication.map.menu.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuDto(
    val placeId: Long? = null,
    val id: Long?,
    val name: String,
    val price: Int,
    val description: String,
    val imageUrl: String
)
