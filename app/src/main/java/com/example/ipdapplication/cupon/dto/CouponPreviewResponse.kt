package com.example.ipdapplication.cupon.dto

import kotlinx.serialization.Serializable

@Serializable
data class CouponPreviewResponse(
    val orderPrice: Int,
    val discountAmount: Int,
    val finalPrice: Int
)
