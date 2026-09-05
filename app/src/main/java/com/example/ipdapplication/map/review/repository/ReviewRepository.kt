package com.example.ipdapplication.map.review.repository

import com.example.ipdapplication.map.review.api.ReviewApi
import com.example.ipdapplication.map.review.dto.ReviewDto
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Singleton
class ReviewRepository @Inject constructor(
    private val api: ReviewApi,
    private val json: Json
){

    suspend fun getAllReviews(): List<ReviewDto> =
        api.getAllReviews()

    suspend fun getReviewByPlace(placeId: Long): List<ReviewDto> =
        runCatching { api.getReviewsByPlace(placeId) }
            .getOrElse { api.getAllReviews().filter { it.placeId == placeId } }

    suspend fun createReview(
        placeId: Long,
        userId: Long,
        reviewComment: String?,
        bssComment: String?,
        tagIds: List<Long>?,
        imageUrls: List<String>? = null,
        imageFiles: List<File> = emptyList()
    ): ReviewDto {
        val request = ReviewDto(
            bssComment = bssComment,
            reviewComment = reviewComment,
            placeId = placeId,
            tagIds = tagIds,
            userId = userId,
            imageUrls = imageUrls
        )

        val jsonBody = json.encodeToString(request)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val imageParts = imageFiles
            .filter { it.exists() }
            .map { file ->
                MultipartBody.Part.createFormData(
                    name = "images",
                    filename = file.name,
                    body = file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

        return api.createReview(
            images = if (imageParts.isEmpty()) null else imageParts,
            request = jsonBody
        )
    }

    suspend fun updateReview(
        reviewId: Long,
        placeId: Long,
        userId: Long,
        reviewComment: String?,
        bssComment: String?,
        tagIds: List<Long>?,
        imageUrls: List<String>? = null,
        imageFiles: List<File> = emptyList()
    ): ReviewDto {
        val request = ReviewDto(
            bssComment = bssComment,
            reviewComment = reviewComment,
            placeId = placeId,
            tagIds = tagIds,
            userId = userId,
            imageUrls = imageUrls
        )

        val jsonBody = json.encodeToString(request)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val imageParts = imageFiles
            .filter { it.exists() }
            .map { file ->
                MultipartBody.Part.createFormData(
                    name = "images",
                    filename = file.name,
                    body = file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

        return api.updateReview(
            reviewId = reviewId,
            images = if (imageParts.isEmpty()) null else imageParts,
            request = jsonBody
        )
    }

    suspend fun deleteReview(reviewId: Long) {
        api.deleteReview(reviewId)
    }
}
