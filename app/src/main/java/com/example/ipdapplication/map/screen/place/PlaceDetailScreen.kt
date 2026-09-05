package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class PlaceDetailData(
    val id: Long,
    val name: String,
    val batteryCount: Int,
    val isFavorite: Boolean,
    val address: String,
    val businessStatus: String,
    val lastOrderTime: String?,
    val phoneNumber: String,
    val menuItems: List<MenuItem>,
    val reviewSummary: ReviewSummary,
    val reviews: List<ReviewItem>,
    val mainImageUrl: String? = null,
    val subImageUrls: List<String> = emptyList(),
    val totalImageCount: Int = 0,
    val likeCount: Int = 0
)

@Composable
fun PlaceDetailScreen(
    placeData: PlaceDetailData,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDepartureClick: () -> Unit,
    onArrivalClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onBatterySwapClick: () -> Unit,
    onMenuViewAllClick: () -> Unit,
    onReviewViewAllClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(PlaceTab.HOME) }
    var sortOption by remember { mutableStateOf("최신순") }

    PlaceDetailScreenContent(
        placeData = placeData,
        selectedTab = selectedTab,
        sortOption = sortOption,
        onBackClick = onBackClick,
        onCloseClick = onCloseClick,
        onFavoriteClick = onFavoriteClick,
        onDepartureClick = onDepartureClick,
        onArrivalClick = onArrivalClick,
        onTabSelected = { selectedTab = it },
        onWriteReviewClick = onWriteReviewClick,
        onBatterySwapClick = onBatterySwapClick,
        onMenuViewAllClick = {
            selectedTab = PlaceTab.MENU
            onMenuViewAllClick()
        },
        onReviewViewAllClick = {
            selectedTab = PlaceTab.REVIEW
            onReviewViewAllClick()
        },
        onSortOptionClick = { /* TODO: sort option bottom sheet */ }
    )
}

@Composable
fun PlaceDetailScreenContent(
    placeData: PlaceDetailData,
    selectedTab: PlaceTab,
    sortOption: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDepartureClick: () -> Unit,
    onArrivalClick: () -> Unit,
    onTabSelected: (PlaceTab) -> Unit,
    onWriteReviewClick: () -> Unit,
    onBatterySwapClick: () -> Unit,
    onMenuViewAllClick: () -> Unit,
    onReviewViewAllClick: () -> Unit,
    onSortOptionClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                PlaceImageGallery(
                    mainImageUrl = placeData.mainImageUrl,
                    subImageUrls = placeData.subImageUrls,
                    totalImageCount = placeData.totalImageCount,
                    onBackClick = onBackClick,
                    onCloseClick = onCloseClick
                )

                PlaceHeader(
                    placeName = placeData.name,
                    batteryCount = placeData.batteryCount,
                    isFavorite = placeData.isFavorite,
                    onFavoriteClick = onFavoriteClick
                )

                PlaceNavigationButtons(
                    onDepartureClick = onDepartureClick,
                    onArrivalClick = onArrivalClick
                )

                Spacer(modifier = Modifier.height(20.dp))

                PlaceTabMenu(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )

                Divider(
                    color = PlaceGrayBorder,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                when (selectedTab) {
                    PlaceTab.HOME -> {
                        PlaceHomeContent(
                            address = placeData.address,
                            businessStatus = placeData.businessStatus,
                            lastOrderTime = placeData.lastOrderTime,
                            phoneNumber = placeData.phoneNumber,
                            likeCount = placeData.likeCount
                        )

                        Divider(
                            color = PlaceGrayBorder,
                            thickness = 8.dp
                        )

                        PlaceMenuContent(
                            menuItems = placeData.menuItems.take(4),
                            onViewAllClick = onMenuViewAllClick
                        )

                        Divider(
                            color = PlaceGrayBorder,
                            thickness = 8.dp
                        )

                        PlaceReviewContent(
                            reviewSummary = placeData.reviewSummary,
                            reviews = placeData.reviews.take(2),
                            onWriteReviewClick = onWriteReviewClick,
                            onViewAllClick = onReviewViewAllClick,
                            sortOption = sortOption,
                            onSortOptionClick = onSortOptionClick
                        )
                    }

                    PlaceTab.MENU -> {
                        PlaceMenuContent(
                            menuItems = placeData.menuItems,
                            onViewAllClick = onMenuViewAllClick
                        )
                    }

                    PlaceTab.REVIEW -> {
                        PlaceReviewContent(
                            reviewSummary = placeData.reviewSummary,
                            reviews = placeData.reviews,
                            onWriteReviewClick = onWriteReviewClick,
                            onViewAllClick = onReviewViewAllClick,
                            sortOption = sortOption,
                            onSortOptionClick = onSortOptionClick
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp)
            ) {
                BatterySwapButton(onClick = onBatterySwapClick)
            }
        }
    }
}

private val previewMenuItems = listOf(
    MenuItem(
        name = "매콤 맛볼래 세트",
        description = "부채살 스테이크와 구운 채소가 담긴 든든한 세트",
        price = 18900
    ),
    MenuItem(
        name = "로제 파스타",
        description = "크림과 토마토의 조합으로 부드럽게 즐기는 로제 파스타",
        price = 19900
    ),
    MenuItem(
        name = "마라 로제 떡볶이",
        description = "마라 소스와 로제가 어우러진 매콤한 떡볶이",
        price = 21900
    ),
    MenuItem(
        name = "감자튀김",
        description = "",
        price = 5900
    )
)

private val previewReviewSummary = ReviewSummary(
    placeLabel = "지점 평점",
    placeTopTag = "친절해요",
    placeProgress = 0.7f,
    bssLabel = "BSS 상태",
    bssTopTag = "충전 빠름",
    bssProgress = 0.85f
)

private val previewReviews = listOf(
    ReviewItem(
        id = 1,
        userName = "김캡틴",
        userGrade = "VVIP 등급",
        userGradeColor = PlaceMintGreen,
        imageUrl = null,
        imageUrls = listOf("https://example.invalid/images/400/200?detail1"),
        placeReview = "직원분들이 친절하고 공간이 깔끔해서 자주 오고 싶어요.",
        bssReview = "배터리 충전 속도가 빨라서 금방 교체할 수 있었어요.",
        tags = listOf("친절해요", "배터리 빨라요", "깔끔해요"),
        likeCount = 9
    ),
    ReviewItem(
        id = 2,
        userName = "홍길동",
        userGrade = "SILVER 등급",
        userGradeColor = Color.Gray,
        imageUrl = null,
        imageUrls = listOf("https://example.invalid/images/300/200?detail2"),
        placeReview = "분위기가 좋고 음식이 맛있어요.",
        bssReview = "주차도 편하고 배터리 교체도 금방 끝났습니다.",
        tags = listOf("맛있어요", "편리해요"),
        likeCount = 5
    )
)

private val previewPlaceData = PlaceDetailData(
    id = 1,
    name = "샘플 식당",
    batteryCount = 2,
    isFavorite = true,
    address = "전시용 가상 주소",
    businessStatus = "영업 중",
    lastOrderTime = "21:00",
    phoneNumber = "000-0000-0000",
    menuItems = previewMenuItems,
    reviewSummary = previewReviewSummary,
    reviews = previewReviews,
    totalImageCount = 12
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PlaceDetailScreenHomePreview() {
    PlaceDetailScreenContent(
        placeData = previewPlaceData,
        selectedTab = PlaceTab.HOME,
        sortOption = "최신순",
        onBackClick = {},
        onCloseClick = {},
        onFavoriteClick = {},
        onDepartureClick = {},
        onArrivalClick = {},
        onTabSelected = {},
        onWriteReviewClick = {},
        onBatterySwapClick = {},
        onMenuViewAllClick = {},
        onReviewViewAllClick = {},
        onSortOptionClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PlaceDetailScreenMenuPreview() {
    PlaceDetailScreenContent(
        placeData = previewPlaceData,
        selectedTab = PlaceTab.MENU,
        sortOption = "최신순",
        onBackClick = {},
        onCloseClick = {},
        onFavoriteClick = {},
        onDepartureClick = {},
        onArrivalClick = {},
        onTabSelected = {},
        onWriteReviewClick = {},
        onBatterySwapClick = {},
        onMenuViewAllClick = {},
        onReviewViewAllClick = {},
        onSortOptionClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PlaceDetailScreenReviewPreview() {
    PlaceDetailScreenContent(
        placeData = previewPlaceData,
        selectedTab = PlaceTab.REVIEW,
        sortOption = "최신순",
        onBackClick = {},
        onCloseClick = {},
        onFavoriteClick = {},
        onDepartureClick = {},
        onArrivalClick = {},
        onTabSelected = {},
        onWriteReviewClick = {},
        onBatterySwapClick = {},
        onMenuViewAllClick = {},
        onReviewViewAllClick = {},
        onSortOptionClick = {}
    )
}
