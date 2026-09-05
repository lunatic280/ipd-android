package com.example.ipdapplication.map.review.api

import com.example.ipdapplication.map.review.dto.TagDto
import retrofit2.http.GET

interface TagApi {

    @GET("tag")
    suspend fun getAllTags(): List<TagDto>
}
