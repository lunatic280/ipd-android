package com.example.ipdapplication.qr.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenSlotResponse(
    val success: Boolean,
    val message: String
)
