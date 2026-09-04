package com.example.ipdapplication.qr.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenSlotRequest(
    val bssId: String,
    val slot: Int
)
