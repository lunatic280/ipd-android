package com.example.ipdapplication.community.Api

import com.example.ipdapplication.community.dto.BlogLikeDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BlogLikeApi {

    @POST("/blogLike/{blogId}/likes/toggle")
    suspend fun toggleBlogLike(
        @Path("blogId") blogId: Long
    ): BlogLikeDto

    @GET("/blogLike/{blogId}/like/status")
    suspend fun getBlogLikeStatus(
        @Path("blogId") blogId: Long
    ): BlogLikeDto
}
