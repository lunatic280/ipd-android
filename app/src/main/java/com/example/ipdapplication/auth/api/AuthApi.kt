package com.example.ipdapplication.auth.api

import com.example.ipdapplication.auth.dto.AuthResponse
import com.example.ipdapplication.auth.dto.LoginRequest
import com.example.ipdapplication.auth.dto.SignRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("auth/signup")
    suspend fun signup(
        @Body request: SignRequest
    ): Long
}
