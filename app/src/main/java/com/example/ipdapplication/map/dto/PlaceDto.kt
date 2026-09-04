package com.example.ipdapplication.map.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val imageUrls: List<String> = emptyList(),
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("images")
    val images: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("mainImageUrl")
    val mainImageUrl: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    val likeCount: Int = 0,
    val telNumber: String? = null,
    val type: String? = null
)
