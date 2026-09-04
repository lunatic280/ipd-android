package com.example.ipdapplication.map.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceLikeDto(
    val liked: Boolean,
    val likeCount: Long
)
