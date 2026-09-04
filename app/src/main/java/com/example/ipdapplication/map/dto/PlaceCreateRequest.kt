package com.example.ipdapplication.map.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceCreateRequest(
    val title: String,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val telNumber: String? = null,
    val type: String? = null
)
