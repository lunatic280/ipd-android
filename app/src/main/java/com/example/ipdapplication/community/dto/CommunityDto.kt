package com.example.ipdapplication.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class BlogDto(
    val id: Long? = null,
    val title: String,
    val content: String,
    val star: Int
)

@Serializable
data class BlogCommentDto(
    val id: Long? = null,
    val blogId: Long,
    val content: String,
    val star: Int
)