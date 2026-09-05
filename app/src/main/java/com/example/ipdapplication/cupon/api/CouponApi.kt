package com.example.ipdapplication.cupon.api

import com.example.ipdapplication.cupon.dto.*
import retrofit2.http.*

interface CouponApi {

    // 사용자: 내 쿠폰 목록 조회
    @GET("coupon/")
    suspend fun getMyCoupons(
        @Query("status") status: String? = null // "AVAILABLE" 등, 없으면 전체
    ): List<UserCouponResponse>

    // 사용자: 할인 미리보기
    @POST("coupon/{userCouponId}/preview")
    suspend fun previewDiscount(
        @Path("userCouponId") userCouponId: Long,
        @Body request: CouponPreviewRequest
    ): CouponPreviewResponse

    // 사용자: 쿠폰 발급 (BSS 교체 완료 시 호출)
    @POST("coupon/create/{policyId}/issue")
    suspend fun issueCoupon(
        @Path("policyId") policyId: Long,
        @Query("source") source: String  // "BSS", "GCOO" 등
    ): UserCouponResponse

    // 관리자: 정책 생성 + 특정 유저에게 발급
    @POST("admin/coupons/create-and-issue")
    suspend fun adminCreateAndIssue(
        @Body request: AdminCreateAndIssueCouponRequest
    ): UserCouponResponse
}
