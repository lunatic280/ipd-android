package com.example.ipdapplication.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStore.getToken()

        val requestBuilder = chain.request().newBuilder()

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)

        // 서버가 401을 반환하면, 저장된 토큰은 더 이상 유효하지 않은 것으로 보고 삭제
        if (response.code == 401) {
            tokenStore.clearToken()
        }

        return response
    }
}
