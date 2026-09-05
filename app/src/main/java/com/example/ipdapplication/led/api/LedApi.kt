package com.example.ipdapplication.led.api

import com.example.ipdapplication.led.dto.LedStatusDto
import retrofit2.http.*

interface LedApi {
    @POST("/led/{deviceId}/change")
    suspend fun changeLed(
        @Path("deviceId") deviceId: String,
        @Body dto: LedStatusDto
    ): LedStatusDto
}
