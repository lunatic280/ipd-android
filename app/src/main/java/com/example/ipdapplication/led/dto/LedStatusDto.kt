package com.example.ipdapplication.led.dto

import kotlinx.serialization.Serializable

@Serializable
data class LedStatusDto(
    val color: String  // "OFF", "RED", "GREEN", "BLUE", "YELLOW", "WHITE", "PURPLE", "CYAN"
)
