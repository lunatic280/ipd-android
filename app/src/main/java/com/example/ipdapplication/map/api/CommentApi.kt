package com.example.ipdapplication.map.api

import com.example.ipdapplication.map.dto.CommentCreateRequest
import com.example.ipdapplication.map.dto.CommentDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface CommentApi {
    @POST("/comment/add")
    suspend fun addComment(@Body body: CommentCreateRequest): CommentDto

    @DELETE("/comment/delete/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Long)

    @GET("/comment/{placeId}")
    suspend fun getAllComment(@Path("placeId") placeId: Long): List<CommentDto>
}
