package com.example.ipdapplication.map.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    val id: Long? = null,
    val placeId: Long,
    val content: String,
)

@Serializable
data class CommentCreateRequest(
    val placeId: Long,
    val content: String,
)
