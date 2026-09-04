package com.example.ipdapplication.map.dto

@kotlinx.serialization.Serializable
data class DirectionsForAppResponse(
    val code: Int,
    val message: String,
    val distance: Int,
    val duration: Long,
    val path: List<LatLngDto>,
    val guides: List<GuideDto>
)

@kotlinx.serialization.Serializable
data class LatLngDto(
    val lat: Double,
    val lng: Double
)

@kotlinx.serialization.Serializable
data class GuideDto(
    val distance: Int,
    val duration: Long,
    val instructions: String,
    val pointIndex: Int,
    val type: Int
)
