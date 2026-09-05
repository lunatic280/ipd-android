package com.example.ipdapplication.map.review.repository

import com.example.ipdapplication.map.review.api.TagApi
import com.example.ipdapplication.map.review.dto.TagDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepository @Inject constructor(
    private val api: TagApi
) {
    suspend fun getAllTags(): List<TagDto> = api.getAllTags()
}
