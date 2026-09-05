package com.example.ipdapplication.map.repository

import com.example.ipdapplication.map.api.PlaceLikeApi
import com.example.ipdapplication.map.dto.PlaceLikeDto
import javax.inject.Inject

class PlaceLikeRepository @Inject constructor(
    private val api: PlaceLikeApi
) {

    suspend fun getStatus(placeId: Long): PlaceLikeDto =
        api.getPlaceLikeStatus(placeId)

    suspend fun toggle(placeId: Long): PlaceLikeDto =
        api.togglePlaceLike(placeId)
}
