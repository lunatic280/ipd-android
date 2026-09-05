package com.example.ipdapplication.qr.repository

import com.example.ipdapplication.qr.api.QrApi
import com.example.ipdapplication.qr.dto.OpenSlotRequest
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrRepository @Inject constructor(
    private val api: QrApi
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun sendQrData(qrData: String): Result<Unit> {
        return runCatching {
            // QR 코드 데이터 파싱 (예: "bssId:slot" 또는 JSON 형식)
            val request = parseQrData(qrData)
            api.openBss(request)
        }
    }

    suspend fun sendNextStep(qrData: String): Result<Unit> {
        return runCatching {
            // QR 코드 데이터 파싱 (예: "bssId:slot" 또는 JSON 형식)
            val request = parseQrData(qrData)
            api.nextStep(request)
        }
    }

    // 전시용 파싱 예제입니다. 실제 연동에는 형식/길이 제한, 장치 권한과 재전송 방지가 필요합니다.
    private fun parseQrData(qrData: String): OpenSlotRequest {
        return try {
            // JSON 형식 시도
            json.decodeFromString<OpenSlotRequest>(qrData)
        } catch (e: Exception) {
            // URL 형식 체크 (http:// 또는 https://)
            if (qrData.startsWith("http://") || qrData.startsWith("https://")) {
                // URL에서 쿼리 파라미터 추출 시도
                val uri = android.net.Uri.parse(qrData)
                val bssIdFromQuery = uri.getQueryParameter("bssId")
                val slotFromQuery = uri.getQueryParameter("slot")?.toIntOrNull()

                if (bssIdFromQuery != null) {
                    // 쿼리 파라미터에서 추출 성공
                    OpenSlotRequest(
                        bssId = bssIdFromQuery,
                        slot = slotFromQuery ?: 1
                    )
                } else {
                    // URL 경로의 마지막 부분을 bssId로 사용
                    val pathSegments = uri.pathSegments
                    val bssId = if (pathSegments.isNotEmpty()) {
                        pathSegments.last()
                    } else {
                        // 경로가 없으면 전체 URL을 사용
                        qrData
                    }
                    OpenSlotRequest(
                        bssId = bssId,
                        slot = 1
                    )
                }
            } else {
                // "bssId:slot" 형식 시도 (URL이 아닌 경우에만)
                val parts = qrData.split(":")
                if (parts.size >= 2 && !qrData.startsWith("http")) {
                    OpenSlotRequest(
                        bssId = parts[0],
                        slot = parts[1].toIntOrNull() ?: 1
                    )
                } else {
                    // 기본값 (QR 데이터 전체를 bssId로 사용)
                    OpenSlotRequest(
                        bssId = qrData,
                        slot = 1
                    )
                }
            }
        }
    }
}
