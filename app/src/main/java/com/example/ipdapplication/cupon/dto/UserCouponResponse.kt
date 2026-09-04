package com.example.ipdapplication.cupon.dto

import kotlinx.serialization.Serializable

// 서버에서 내려오는 "내 쿠폰" 응답
@Serializable
data class UserCouponResponse(
    val id: Long,
    val name: String,
    val discountAmount: Int,
    val description: String,
    val source: String,      // "GCOO", "BSS" (서버 enum 문자열)
    val status: String,      // "AVAILABLE", "USED", "EXPIRED"
    val expiredAt: String    // ISO 문자열 (예: 2025-12-03T00:00:00)
)
