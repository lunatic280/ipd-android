package com.example.ipdapplication.cupon.dto

import kotlinx.serialization.Serializable

@Serializable
data class CouponData(
    val name: String,        // 쿠폰 이름
    val price: String,       // 쿠폰 가격 (예: "2,000원")
    val description: String, // 쿠폰 설명
    val source: String       // 어디서 받은 쿠폰인지 (예: "지쿠", "BSS")
)
