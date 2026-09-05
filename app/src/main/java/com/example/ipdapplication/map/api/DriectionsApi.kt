package com.example.ipdapplication.map.api

import com.example.ipdapplication.map.dto.DirectionsForAppResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApi {
    @GET("directions")
    suspend fun getDirections(
        @Query("startLat") startLat: Double,
        @Query("startLng") startLng: Double,
        @Query("goalLat") goalLat: Double,
        @Query("goalLng") goalLng: Double,
    ): DirectionsForAppResponse
}
