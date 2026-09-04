package com.example.ipdapplication.map.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val sido: String,
    val sigungu: String,
    val dong: String,
    val building: String
)
