package com.example.ipdapplication.community.Api

import com.example.ipdapplication.community.dto.BlogDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BlogApi {

    @GET("/blog/")
    suspend fun getAllBlogs(): List<BlogDto>

    @GET("/blog/{id}")
    suspend fun getBlogById(
        @Path("id") id: Long
    ): BlogDto

    @POST("/blog/create")
    suspend fun createBlog(
        @Body blogDto: BlogDto
    ): BlogDto

    @PATCH("/blog/update/{id}")
    suspend fun updateBlog(
        @Path("id") id: Long,
        @Body blogDto: BlogDto
    ): BlogDto

    @DELETE("/blog/delete/{id}")
    suspend fun deleteBlog(
        @Path("id") id: Long
    )
}
