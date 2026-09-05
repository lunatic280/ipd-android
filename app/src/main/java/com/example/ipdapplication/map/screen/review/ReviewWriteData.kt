package com.example.ipdapplication.map.screen.review

import android.net.Uri

/**
 * 리뷰 작성 데이터 모델
 */
data class ReviewWriteData(
    val placeTagIds: List<Long>,
    val bssTagIds: List<Long>,
    val placeReview: String,
    val bssReview: String,
    val photoUris: List<Uri>
)
