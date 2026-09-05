package com.example.ipdapplication.map.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ipdapplication.map.screen.review.MenuItemUi
import com.example.ipdapplication.map.screen.review.MenuTab

@Composable
fun PlaceBottomDetailSheet(
    modifier: Modifier = Modifier,
    menuItems: List<MenuItemUi> = sampleMenuItems,
    reviewSummary: ReviewSummaryData = sampleReviewSummary,
    reviews: List<ReviewData> = defaultReviews
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Surface(
        modifier = modifier,
        tonalElevation = 2.dp,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            PlaceTabNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            when (selectedTab) {
                0 -> HomeTab()
                1 -> MenuTab(menuItems = menuItems)
                2 -> ReviewContentSection(
                    reviewSummary = reviewSummary,
                    reviews = reviews
                )
            }
        }
    }
}

@Composable
fun PlaceTabNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PlaceTabItem(
            text = "Home",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        PlaceTabItem(
            text = "Menu",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        PlaceTabItem(
            text = "Reviews",
            isSelected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}

@Composable
fun PlaceTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF00C46E) else Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(
                        color = Color(0xFF00C46E),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun HomeTab(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        HeaderSection()
        Spacer(Modifier.height(16.dp))
        InfoTexts()
        Spacer(Modifier.height(14.dp))
        PriceChip()
        Spacer(Modifier.height(18.dp))
        PhotoPreviewRow()
        Spacer(Modifier.height(20.dp))
        ShortcutRow()
        Spacer(Modifier.height(12.dp))
        ActionButtons()
    }
}

@Composable
fun ReviewContentSection(
    reviewSummary: ReviewSummaryData,
    reviews: List<ReviewData>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ReviewSummarySection(reviewSummary = reviewSummary)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(reviews) { review ->
            ReviewCard(review = review)
        }
    }
}

@Composable
private fun ReviewSummarySection(reviewSummary: ReviewSummaryData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reviews ${reviewSummary.totalReviews}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Sort",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Best",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF8E8E8E)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                contentDescription = "Collapse",
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF8E8E8E)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        pushStyle(SpanStyle(color = Color(0xFF00C46E), fontWeight = FontWeight.Bold))
                        append("Place ")
                        pop()
                        append(review.placeReview)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
                Text(
                    text = buildAnnotatedString {
                        pushStyle(SpanStyle(color = Color(0xFF00C46E), fontWeight = FontWeight.Bold))
                        append("BSS ")
                        pop()
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Comments",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF8E8E8E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Comments ${review.commentCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8E8E8E)
                )
            }
        }
    }
}

private val sampleMenuItems = listOf(
    MenuItemUi(
        id = 1,
        name = "Signature Ramen",
        description = "Deep broth with plenty of toppings.",
        price = "12,000 KRW"
    ),
    MenuItemUi(
        id = 2,
        name = "Miso Ramen",
        description = "Light miso base with soft noodles.",
        price = "11,000 KRW"
    ),
    MenuItemUi(
        id = 3,
        name = "Chashu Ramen",
        description = "Hearty bowl with grilled chashu.",
        price = "13,000 KRW"
    )
)

private val sampleReviewSummary = ReviewSummaryData(
    totalReviews = 2532,
    ratingBars = listOf(
        RatingBarData("Place rating", 70),
        RatingBarData("BSS rating", 90)
    )
)

private val defaultReviews = listOf(
    ReviewData(
        id = 1,
        userName = "Sample User",
        date = "25.11.22",
        placeReview = "Nice atmosphere and reasonable prices.",
        bssReview = "Battery swap was convenient.",
        tags = listOf("Tasty", "Kind", "Value"),
        commentCount = 9
    )
)

@Preview(showBackground = true)
@Composable
private fun PlaceBottomDetailSheetPreview() {
    PlaceBottomDetailSheet()
}

@Preview(showBackground = true)
@Composable
private fun PlaceTabNavigationPreview() {
    PlaceTabNavigation(selectedTab = 0, onTabSelected = {})
}

@Preview(showBackground = true)
@Composable
private fun HomeTabPreview() {
    HomeTab()
}
