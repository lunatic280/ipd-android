package com.example.ipdapplication.map.api

import com.example.ipdapplication.map.dto.PlaceDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PlaceApi {
    @GET("/place/")
    suspend fun getPlaces(): List<PlaceDto>

    @Multipart
    @POST("/place/create")
    suspend fun createPlaceWithImage(
        @Part images: List<MultipartBody.Part>,
        @Part("request") request: RequestBody
    ): PlaceDto

    @PATCH("/place/update/{id}")
    suspend fun updatePlace(
        @Path("id") id: Long,
        @Body request: PlaceDto
    ): PlaceDto

    @DELETE("/place/delete/{id}")
    suspend fun deletePlace(@Path("id") id: Long)
}
