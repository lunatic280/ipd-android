package com.example.ipdapplication.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class BlogLikeDto(
    val liked: Boolean,
    val likeCount: Long
)
