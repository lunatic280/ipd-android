package com.example.ipdapplication.auth.repository

import com.example.ipdapplication.auth.api.AuthApi
import com.example.ipdapplication.auth.dto.AuthResponse
import com.example.ipdapplication.auth.dto.LoginRequest
import com.example.ipdapplication.auth.dto.SignRequest
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi
) {

    suspend fun login(email: String, password: String): Result<AuthResponse> =
        runCatching {
            api.login(LoginRequest(email, password))
        }

    suspend fun signUp(
        email: String,
        password: String,
        name: String
    ): Result<Long> =
        runCatching {
            api.signup(SignRequest(email, password, name))
        }
}
