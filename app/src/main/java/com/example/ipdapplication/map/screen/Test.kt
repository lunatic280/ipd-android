package com.example.ipdapplication.map.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ReviewData(
    val id: Long,
    val userName: String,
    val date: String,
    val placeReview: String,
    val bssReview: String,
    val tags: List<String>,
    val commentCount: Int
)

data class ReviewSummaryData(
    val totalReviews: Int,
    val ratingBars: List<RatingBarData>
)

data class RatingBarData(
    val label: String,
    val percentage: Int
)

data class RestaurantData(
    val name: String,
    val description: String,
    val category: String,
    val reviewCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    restaurant: RestaurantData,
    reviewSummary: ReviewSummaryData,
    reviews: List<ReviewData>,
    onBack: () -> Unit = {}
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            RestaurantHeaderSection(restaurant = restaurant)
            Spacer(Modifier.height(8.dp))
            ReviewSummarySection(reviewSummary)
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }
            }
        }
    }
}

@Composable
private fun RestaurantHeaderSection(restaurant: RestaurantData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF171717)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = restaurant.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B4B4B)
                )
            }
            Surface(
                shape = CircleShape,
                color = Color(0xFFDFF7EB),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "즐겨찾기",
                    tint = Color(0xFF00C46E),
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${restaurant.category} · 리뷰 ${restaurant.reviewCount}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                onClick = { }
            ) {
                Text(
                    text = "출발",
                    color = Color(0xFF00C46E),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C46E),
                    contentColor = Color.White
                ),
                onClick = { }
            ) {
                Text(
                    text = "도착",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ReviewSummarySection(reviewSummary: ReviewSummaryData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "리뷰 ${reviewSummary.totalReviews}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "정렬",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "베스트순",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF8E8E8E)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            reviewSummary.ratingBars.forEach { ratingBar ->
                RatingBarItem(ratingBar = ratingBar)
            }
        }
    }
}

@Composable
private fun RatingBarItem(ratingBar: RatingBarData) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ratingBar.label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF363636)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "접기",
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF8E8E8E)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        androidx.compose.material3.LinearProgressIndicator(
            progress = { ratingBar.percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color(0xFF00C46E),
            trackColor = Color(0xFFE4E4E4)
        )
    }
}

@Composable
private fun ReviewCard(review: ReviewData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color(0xFFE4E4E4)
                ) {}

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.userName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = review.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8E8E8E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = buildAnnotatedString {
                        addStyle(
                            SpanStyle(
                                color = Color(0xFF00C46E),
                                fontWeight = FontWeight.Bold
                            ),
                            start = 0,
                            end = 5
                        )
                        append("장소 리뷰 ")
                        append(review.placeReview)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )

                Text(
                    text = buildAnnotatedString {
                        addStyle(
                            SpanStyle(
                                color = Color(0xFF00C46E),
                                fontWeight = FontWeight.Bold
                            ),
                            start = 0,
                            end = 5
                        )
                        append("BSS 리뷰 ")
                        append(review.bssReview)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                review.tags.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF2F4F6)
                    ) {
                        Text(
                            text = tag,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF363636)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "댓글",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF8E8E8E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "댓글 ${review.commentCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8E8E8E)
                )
            }
        }
    }
}

private val defaultReviews = listOf(
    ReviewData(
        id = 1,
        userName = "샘플 이용자",
        date = "25.11.22",
        placeReview = "가게 분위기와 맛이 좋았어요.",
        bssReview = "배달 서비스도 만족스러웠습니다.",
        tags = listOf("맛있어요", "친절해요", "가성비굿"),
        commentCount = 9
    )
)

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    val restaurant = RestaurantData(
        name = "샘플 레스토랑",
        description = "3개의 베스트 메뉴를 가진 캐주얼 레스토랑",
        category = "한식",
        reviewCount = 2532
    )
    val summary = ReviewSummaryData(
        totalReviews = 2532,
        ratingBars = listOf(
            RatingBarData("맛 만족도", 70),
            RatingBarData("서비스 만족도", 90)
        )
    )
    ReviewScreen(
        restaurant = restaurant,
        reviewSummary = summary,
        reviews = defaultReviews
    )
}
