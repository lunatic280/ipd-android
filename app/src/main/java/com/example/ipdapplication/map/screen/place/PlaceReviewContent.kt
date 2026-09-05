package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

data class ReviewSummary(
    val placeLabel: String,
    val placeTopTag: String,
    val placeProgress: Float,
    val bssLabel: String,
    val bssTopTag: String,
    val bssProgress: Float
)

data class ReviewItem(
    val id: Long,
    val userName: String,
    val userGrade: String,
    val userGradeColor: Color,
    val imageUrl: String? = null,
    val imageUrls: List<String> = emptyList(),
    val placeReview: String,
    val bssReview: String,
    val tags: List<String>,
    val likeCount: Int
)

@Composable
fun PlaceReviewContent(
    reviewSummary: ReviewSummary,
    reviews: List<ReviewItem>,
    onWriteReviewClick: () -> Unit,
    onViewAllClick: () -> Unit,
    sortOption: String,
    onSortOptionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "리뷰",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PlaceDarkText
            )

            Row(
                modifier = Modifier.clickable(onClick = onSortOptionClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sortOption,
                    fontSize = 14.sp,
                    color = PlaceGrayText
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "정렬",
                    tint = PlaceGrayText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ReviewSummarySection(summary = reviewSummary)

        Spacer(modifier = Modifier.height(20.dp))

        WriteReviewButton(onClick = onWriteReviewClick)

        Spacer(modifier = Modifier.height(20.dp))

        reviews.forEach { review ->
            PlaceReviewListItem(review = review)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (reviews.isNotEmpty()) {
            Divider(color = PlaceGrayBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onViewAllClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "전체보기",
                    fontSize = 14.sp,
                    color = PlaceGrayText
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "전체보기",
                    tint = PlaceGrayText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ReviewSummarySection(
    summary: ReviewSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReviewProgressRow(
            label = summary.placeLabel,
            tag = summary.placeTopTag,
            progress = summary.placeProgress
        )

        ReviewProgressRow(
            label = summary.bssLabel,
            tag = summary.bssTopTag,
            progress = summary.bssProgress
        )
    }
}

@Composable
fun ReviewProgressRow(
    label: String,
    tag: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val percentage = (progress * 100).toInt()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PlaceLabelGreen,
            modifier = Modifier.width(90.dp)
        )

        Column(
            modifier = Modifier.width(100.dp)
        ) {
            Text(
                text = tag,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PlaceDarkText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$percentage%",
                fontSize = 12.sp,
                color = PlaceGrayText
            )
        }

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = PlaceMintGreen,
            trackColor = PlaceGrayBorder
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "자세히 보기",
            tint = PlaceGrayText,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )
    }
}

@Composable
fun WriteReviewButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "리뷰 작성",
            tint = PlaceDarkText,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "리뷰 작성",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = PlaceDarkText
        )
    }
}

@Composable
fun PlaceReviewListItem(
    review: ReviewItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PlaceGrayBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PlaceGrayBorder),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "사용자",
                    tint = PlaceGrayText
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = review.userName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PlaceDarkText
                )
                Text(
                    text = review.userGrade,
                    fontSize = 12.sp,
                    color = review.userGradeColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val imagesToShow = if (review.imageUrls.isNotEmpty()) review.imageUrls else listOfNotNull(review.imageUrl)
        if (imagesToShow.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(imagesToShow) { url ->
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PlaceLightMint)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .build(),
                            contentDescription = "리뷰 이미지",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = "지점 리뷰",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PlaceLabelGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.placeReview,
            fontSize = 14.sp,
            color = PlaceDarkText,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "BSS 리뷰",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PlaceLabelGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.bssReview,
            fontSize = 14.sp,
            color = PlaceDarkText,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            review.tags.forEach { tag ->
                Text(
                    text = tag,
                    fontSize = 12.sp,
                    color = PlaceGrayText,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = PlaceGrayBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceReviewContentPreview() {
    PlaceReviewContent(
        reviewSummary = ReviewSummary(
            placeLabel = "지점 전체",
            placeTopTag = "친절해요",
            placeProgress = 0.7f,
            bssLabel = "BSS 상태",
            bssTopTag = "충전 빠름",
            bssProgress = 0.85f
        ),
        reviews = listOf(
            ReviewItem(
                id = 1,
                userName = "김테스터",
                userGrade = "VVIP 등급",
                userGradeColor = PlaceMintGreen,
                imageUrl = null,
                imageUrls = listOf(
                    "https://example.invalid/images/400/200?review1",
                    "https://example.invalid/images/400/200?review2"
                ),
                placeReview = "직원분들이 친절하고 공간도 쾌적해서 자주 들러요.",
                bssReview = "배터리 충전 속도가 빨라서 금방 교체할 수 있었어요.",
                tags = listOf("친절해요", "배터리 빨라요", "가성비 좋아요"),
                likeCount = 9
            )
        ),
        onWriteReviewClick = {},
        onViewAllClick = {},
        sortOption = "베스트순",
        onSortOptionClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun WriteReviewButtonPreview() {
    WriteReviewButton(onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun PlaceReviewListItemPreview() {
    PlaceReviewListItem(
        review = ReviewItem(
            id = 1,
            userName = "홍길동",
            userGrade = "SILVER 등급",
            userGradeColor = Color.Gray,
            imageUrl = null,
            imageUrls = listOf(
                "https://example.invalid/images/320/200?preview1",
                "https://example.invalid/images/320/200?preview2"
            ),
            placeReview = "분위기가 좋고 음식도 맛있어요.",
            bssReview = "주차가 편리하고 배터리 교체도 금방 끝났습니다.",
            tags = listOf("맛있어요", "편리해요"),
            likeCount = 5
        )
    )
}
