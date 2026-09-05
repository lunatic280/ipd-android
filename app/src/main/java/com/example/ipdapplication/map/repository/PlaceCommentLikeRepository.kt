package com.example.ipdapplication.map.repository

import com.example.ipdapplication.map.api.PlaceCommentLikeApi
import com.example.ipdapplication.map.dto.PlaceLikeDto
import javax.inject.Inject

class PlaceCommentLikeRepository @Inject constructor(
    private val api: PlaceCommentLikeApi
) {

    suspend fun getStatus(commentId: Long): PlaceLikeDto =
        api.getPlaceCommentLikeStatus(commentId)

    suspend fun toggle(commentId: Long): PlaceLikeDto =
        api.togglePlaceCommentLike(commentId)
}
