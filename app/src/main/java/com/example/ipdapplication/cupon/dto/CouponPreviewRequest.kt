package com.example.ipdapplication.cupon.dto

import kotlinx.serialization.Serializable

// 할인 미리보기 요청/응답
@Serializable
data class CouponPreviewRequest(
    val orderPrice: Int
)
