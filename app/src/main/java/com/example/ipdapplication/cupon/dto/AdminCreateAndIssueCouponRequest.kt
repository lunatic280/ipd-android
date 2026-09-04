package com.example.ipdapplication.cupon.dto

import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale

// 관리자: 정책 생성 + 발급 요청
@Serializable
data class AdminCreateAndIssueCouponRequest(
    val userId: Long,

    val name: String,
    val discountAmount: Int?,
    val discountRate: Int?,
    val minOrderPrice: Int = 0,
    val description: String = "",

    val source: String = "GCOO",  // 서버 enum CouponSource.GCOO
    val validDays: Long = 30
)

// === 네트워크 DTO -> 화면 DTO 매핑 ===

// 숫자(원) -> "2,000원" 포맷
private fun Int.toWonText(): String {
    val nf = NumberFormat.getNumberInstance(Locale.KOREA)
    return nf.format(this) + "원"
}

// 서버 응답 -> 화면용 CouponData 로 변환
fun UserCouponResponse.toCouponData(): CouponData {
    val sourceText = when (source) {
        "GCOO" -> "지쿠"
        "BSS" -> "BSS"
        else -> source
    }

    return CouponData(
        name = name,
        price = discountAmount.toWonText(),
        description = description,
        source = sourceText
    )
}
