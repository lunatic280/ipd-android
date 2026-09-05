package com.example.ipdapplication.map.api

import com.example.ipdapplication.map.dto.PlaceLikeDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlaceLikeApi {

    @GET("/placeLike/{placeId}/like/status")
    suspend fun getPlaceLikeStatus(
        @Path("placeId") placeId: Long
    ): PlaceLikeDto

    @POST("/placeLike/{placeId}/likes/toggle")
    suspend fun togglePlaceLike(
        @Path("placeId") placeId: Long
    ): PlaceLikeDto
}
