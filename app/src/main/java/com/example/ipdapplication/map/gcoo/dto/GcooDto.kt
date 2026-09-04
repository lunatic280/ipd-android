package com.example.ipdapplication.map.gcoo.dto

import kotlinx.serialization.Serializable

@Serializable
data class GcooDto(
    val id: Long? = null,
    val charge: Int,          // 충전량 (배터리 퍼센트)
    val distance: Int,        // 주행 가능 거리 (km)
    val subTitle: String,     // 부제목 (예: "Max Plus - 389417")
    val description: String,  // 설명 (예: "분당 180원")
    val imageUrl: String? = null,
    val latitude: Double,
    val longitude: Double,
    val userId: Long
)
