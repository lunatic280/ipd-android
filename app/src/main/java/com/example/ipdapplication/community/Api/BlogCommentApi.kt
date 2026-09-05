// app/src/main/java/com/example/ipdapplication/community/Api/BlogCommentApi.kt
package com.example.ipdapplication.community.Api

import com.example.ipdapplication.community.dto.BlogCommentDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BlogCommentApi {

    @GET("/blogComment/")
    suspend fun getAllComments(): List<BlogCommentDto>

    @POST("/blogComment/create")
    suspend fun createComment(
        @Body blogCommentDto: BlogCommentDto
    ): BlogCommentDto

    @PATCH("/blogComment/{id}")
    suspend fun updateComment(
        @Path("id") commentId: Long,
        @Body blogCommentDto: BlogCommentDto
    ): BlogCommentDto

    @DELETE("/blogComment/delete/{id}")
    suspend fun deleteComment(
        @Path("id") commentId: Long
    )
}
