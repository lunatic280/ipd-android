package com.example.ipdapplication.map.repository

import com.example.ipdapplication.map.api.CommentApi
import com.example.ipdapplication.map.dto.CommentCreateRequest
import com.example.ipdapplication.map.dto.CommentDto
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val api: CommentApi
){
    suspend fun add(placeId: Long, content: String): CommentDto =
        api.addComment(CommentCreateRequest(placeId, content))

    suspend fun delete(commentId: Long) =
        api.deleteComment(commentId)

    suspend fun getAllComment(placeId: Long): List<CommentDto> =
        api.getAllComment(placeId)
}
