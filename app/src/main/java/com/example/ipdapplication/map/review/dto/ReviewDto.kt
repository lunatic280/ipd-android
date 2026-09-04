package com.example.ipdapplication.map.review.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReviewDto(
    val id: Long? = null,
    val bssComment: String? = null,
    val reviewComment: String? = null,
    val placeId: Long,
    val userId: Long,
    val tagIds: List<Long>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("tags")
    val tags: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val tagNames: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val imageUrl: List<String>? = null,  // 서버에서 배열로 옴
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val imageUrls: List<String>? = null
)
