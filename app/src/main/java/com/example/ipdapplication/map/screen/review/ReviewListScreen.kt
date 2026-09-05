package com.example.ipdapplication.map.screen.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.map.review.dto.ReviewDto
import com.example.ipdapplication.map.review.viewmodel.ReviewModel
import com.example.ipdapplication.map.screen.place.PlaceDarkText
import com.example.ipdapplication.map.screen.place.PlaceGrayBorder
import com.example.ipdapplication.map.screen.place.PlaceGrayText
import com.example.ipdapplication.map.screen.place.PlaceMintGreen
import com.example.ipdapplication.map.screen.place.PlaceReviewListItem
import com.example.ipdapplication.map.screen.place.ReviewItem
import com.example.ipdapplication.map.screen.place.asFullUrl
import com.example.ipdapplication.map.screen.place.asFullUrls
import com.example.ipdapplication.map.screen.place.tagIdToLabel

@Composable
fun ReviewListScreen(
    placeId: Long,
    placeName: String,
    onBackClick: () -> Unit,
    reviewViewModel: ReviewModel = hiltViewModel()
) {
    val reviewState by reviewViewModel.state.collectAsState()

    LaunchedEffect(placeId) {
        reviewViewModel.loadReviewsForPlace(placeId)
    }

    val reviewItems = reviewState.reviews.map { it.toReviewItem() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로 가기"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = placeName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlaceDarkText
                )
            }

            Divider(color = PlaceGrayBorder, thickness = 1.dp)

            when {
                reviewState.loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PlaceMintGreen)
                    }
                }

                reviewItems.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "등록된 리뷰가 없어요.",
                            fontSize = 16.sp,
                            color = PlaceGrayText
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        itemsIndexed(reviewItems) { index, item ->
                            PlaceReviewListItem(review = item)
                            if (index < reviewItems.lastIndex) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun ReviewDto.toReviewItem(): ReviewItem {
    val mergedImages = imageUrls.asFullUrls().ifEmpty { imageUrl.asFullUrls() }
    val image = mergedImages.firstOrNull()
    return ReviewItem(
        id = id ?: 0L,
        userName = "사용자$userId",
        userGrade = "SILVER 등급",
        userGradeColor = Color.Gray,
        imageUrl = image,
        imageUrls = mergedImages,
        placeReview = reviewComment ?: "",
        bssReview = bssComment ?: "",
        tags = resolveReviewTags(this),
        likeCount = 0
    )
}

private fun resolveReviewTags(review: ReviewDto): List<String> {
    val names = review.tags ?: review.tagNames
    if (!names.isNullOrEmpty()) return names

    val fromIds = review.tagIds?.mapNotNull { id ->
        tagIdToLabel[id] ?: "#$id"
    }.orEmpty()

    return fromIds
}

@Preview(showBackground = true)
@Composable
fun ReviewListScreenPreview() {
    val sampleReviews = listOf(
        ReviewItem(
            id = 1L,
            userName = "사용자1",
            userGrade = "GOLD 등급",
            userGradeColor = Color(0xFFFFD700),
            imageUrl = null,
            imageUrls = emptyList(),
            placeReview = "정말 좋은 장소입니다. 배터리 교환도 빠르고 직원분들도 친절하세요!",
            bssReview = "시설이 깨끗하고 이용하기 편리했어요.",
            tags = listOf("#친절해요", "#깨끗해요", "#빨라요"),
            likeCount = 15
        ),
        ReviewItem(
            id = 2L,
            userName = "사용자2",
            userGrade = "SILVER 등급",
            userGradeColor = Color.Gray,
            imageUrl = null,
            imageUrls = emptyList(),
            placeReview = "위치도 좋고 찾기 쉬워요.",
            bssReview = "배터리 상태도 좋습니다.",
            tags = listOf("#위치좋아요", "#찾기쉬워요"),
            likeCount = 8
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로 가기"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "강북역 8번 출구",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlaceDarkText
                )
            }

            Divider(color = PlaceGrayBorder, thickness = 1.dp)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                itemsIndexed(sampleReviews) { index, item ->
                    PlaceReviewListItem(review = item)
                    if (index < sampleReviews.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
