package com.example.ipdapplication.qr.api

import com.example.ipdapplication.qr.dto.OpenSlotRequest
import com.example.ipdapplication.qr.dto.OpenSlotResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface QrApi {

    @POST("qr/openBss")
    suspend fun openBss(
        @Body request: OpenSlotRequest
    ): OpenSlotResponse

    @POST("qr/nextStep")
    suspend fun nextStep(
        @Body request: OpenSlotRequest
    ): OpenSlotResponse
}
