package com.example.ipdapplication.map.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: Long,
    val name: String,
    val tagType: TagType
)

@Serializable
enum class TagType {
    RESTAURANT,  // 식당 리뷰에서만 사용
    BSS,         // BSS 리뷰에서만 사용
    COMMON       // 공통으로 사용
}
