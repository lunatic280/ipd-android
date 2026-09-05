package com.example.ipdapplication.map.review.api

import com.example.ipdapplication.map.review.dto.ReviewDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ReviewApi {

    @GET("review/")
    suspend fun getAllReviews(): List<ReviewDto>

    @GET("review/{placeId}")
    suspend fun getReviewsByPlace(
        @Path("placeId") placeId: Long
    ): List<ReviewDto>

    @Multipart
    @POST("review/create")
    suspend fun createReview(
        @Part images: List<MultipartBody.Part>?,
        @Part("request") request: RequestBody
    ): ReviewDto

    @Multipart
    @PUT("/review/update/{reviewId}")
    suspend fun updateReview(
        @Path("reviewId") reviewId: Long,
        @Part images: List<MultipartBody.Part>?,
        @Part("request") request: RequestBody
    ): ReviewDto

    @DELETE("review/delete/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Long
    )
}
