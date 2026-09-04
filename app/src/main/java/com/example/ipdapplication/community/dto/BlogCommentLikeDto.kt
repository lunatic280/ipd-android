package com.example.ipdapplication.community.dto

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BlogCommentLikeApi {

    @POST("/blogCommentLike/{commentId}/like/toggle")
    suspend fun toggleCommentLike(
        @Path("commentId") commentId: Long
    ): BlogLikeDto

    @GET("/blogCommentLike/{commentId}/like/status")
    suspend fun getCommentLikeStatus(
        @Path("commentId") commentId: Long
    ): BlogLikeDto
}