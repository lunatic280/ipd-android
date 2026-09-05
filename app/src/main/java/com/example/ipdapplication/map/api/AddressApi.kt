package com.example.ipdapplication.map.api

import com.example.ipdapplication.map.dto.AddressDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressApi {

    @GET("/get")
    suspend fun getAddress(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
    ): AddressDto
}
