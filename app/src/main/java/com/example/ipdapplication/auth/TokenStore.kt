package com.example.ipdapplication.auth

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenStore @Inject constructor() {
    // ponytail: 전시용 세션은 메모리에만 보관하며 프로세스 종료 시 사라집니다.
    // 실제 앱에서 로그인 유지가 필요하면 Keystore 기반 저장 및 만료 처리를 검토합니다.
    @Volatile private var sessionToken: String? = null

    fun saveToken(token: String) {
        sessionToken = token
    }

    fun getToken(): String? =
        sessionToken

    fun clearToken() {
        sessionToken = null
    }
}
