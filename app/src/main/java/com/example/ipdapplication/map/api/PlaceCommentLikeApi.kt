package com.example.ipdapplication.map.api

import com.example.ipdapplication.map.dto.PlaceLikeDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlaceCommentLikeApi {

    @GET("placeCommentLike/{commentId}/like/status")
    suspend fun getPlaceCommentLikeStatus(
        @Path("commentId") commentId: Long
    ): PlaceLikeDto

    @POST("/placeCommentLike/{commentId}/like/toggle")
    suspend fun togglePlaceCommentLike(
        @Path("commentId") commentId: Long
    ): PlaceLikeDto
}
