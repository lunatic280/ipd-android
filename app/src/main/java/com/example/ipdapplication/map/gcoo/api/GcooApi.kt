package com.example.ipdapplication.map.gcoo.api

import com.example.ipdapplication.map.gcoo.dto.GcooDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface GcooApi {
    // 전체 Gcoo 목록 조회 (모든 사용자 가능)
    @GET("/gcoo/")
    suspend fun getGcoos(): List<GcooDto>

    // 특정 Gcoo 조회 (모든 사용자 가능)
    @GET("/gcoo/{gcooId}")
    suspend fun getGcooById(@Path("gcooId") gcooId: Long): GcooDto

    // Gcoo 생성 (OWNER만 가능)
    @Multipart
    @POST("/gcoo/create")
    suspend fun createGcoo(
        @Part("request") request: RequestBody,
        @Part image: MultipartBody.Part
    ): GcooDto

    // Gcoo 수정 (OWNER만 가능)
    @Multipart
    @PATCH("/gcoo/{gcooId}")
    suspend fun updateGcoo(
        @Path("gcooId") gcooId: Long,
        @Part("request") request: RequestBody,
        @Part image: MultipartBody.Part?
    ): GcooDto

    // Gcoo 삭제 (OWNER만 가능)
    @DELETE("/gcoo/{gcooId}")
    suspend fun deleteGcoo(@Path("gcooId") gcooId: Long)
}
