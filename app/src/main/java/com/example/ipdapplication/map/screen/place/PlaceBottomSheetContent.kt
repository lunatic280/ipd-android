package com.example.ipdapplication.map.screen.place

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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

@Composable
fun PlaceBottomSheetContent(
    placeData: PlaceDetailData,
    isExpanded: Boolean,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDepartureClick: () -> Unit,
    onArrivalClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onBatterySwapClick: () -> Unit,
    onMenuViewAllClick: () -> Unit,
    onReviewViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(PlaceTab.HOME) }
    var sortOption by remember { mutableStateOf("최신순") }
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isExpanded) {
                            Modifier.verticalScroll(scrollState)
                        } else {
                            Modifier
                        }
                    )
            ) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    PlaceImageGallery(
                        mainImageUrl = placeData.mainImageUrl,
                        subImageUrls = placeData.subImageUrls,
                        totalImageCount = placeData.totalImageCount,
                        onBackClick = onBackClick,
                        onCloseClick = onCloseClick
                    )
                }

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
                    onTabSelected = { selectedTab = it }
                )

                Divider(
                    color = PlaceGrayBorder,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                if (!isExpanded) {
                    PlaceHomeContent(
                        address = placeData.address,
                        businessStatus = placeData.businessStatus,
                        lastOrderTime = placeData.lastOrderTime,
                        phoneNumber = placeData.phoneNumber,
                        likeCount = placeData.likeCount
                    )
                } else {
                    when (selectedTab) {
                        PlaceTab.HOME -> {
                            PlaceHomeContent(
                                address = placeData.address,
                                businessStatus = placeData.businessStatus,
                                lastOrderTime = placeData.lastOrderTime,
                                phoneNumber = placeData.phoneNumber,
                                likeCount = placeData.likeCount
                            )

                            Divider(color = PlaceGrayBorder, thickness = 8.dp)

                            PlaceMenuContent(
                                menuItems = placeData.menuItems.take(4),
                                onViewAllClick = {
                                    selectedTab = PlaceTab.MENU
                                    onMenuViewAllClick()
                                }
                            )

                            Divider(color = PlaceGrayBorder, thickness = 8.dp)

                            PlaceReviewContent(
                                reviewSummary = placeData.reviewSummary,
                                reviews = placeData.reviews.take(2),
                                onWriteReviewClick = onWriteReviewClick,
                                onViewAllClick = {
                                    selectedTab = PlaceTab.REVIEW
                                    onReviewViewAllClick()
                                },
                                sortOption = sortOption,
                                onSortOptionClick = { /* TODO */ }
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
                                onSortOptionClick = { /* TODO */ }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            if (isExpanded) {
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
}

private val previewBottomSheetData = PlaceDetailData(
    id = 1,
    name = "샘플 식당",
    batteryCount = 2,
    isFavorite = true,
    address = "전시용 가상 주소",
    businessStatus = "영업 중",
    lastOrderTime = "21:00",
    phoneNumber = "000-0000-0000",
    menuItems = listOf(
        MenuItem(
            name = "매콤 맛볼래 세트",
            description = "부채살 스테이크와 구운 채소가 담긴 든든한 세트",
            price = 18900
        ),
        MenuItem(
            name = "로제 파스타",
            description = "크림과 토마토의 조합으로 부드럽게 즐기는 로제 파스타",
            price = 19900
        )
    ),
    reviewSummary = ReviewSummary(
        placeLabel = "지점 평점",
        placeTopTag = "친절해요",
        placeProgress = 0.7f,
        bssLabel = "BSS 상태",
        bssTopTag = "충전 빠름",
        bssProgress = 0.85f
    ),
    reviews = listOf(
        ReviewItem(
            id = 1,
            userName = "김캡틴",
            userGrade = "VVIP 등급",
            userGradeColor = PlaceMintGreen,
            imageUrl = null,
            imageUrls = listOf("https://example.invalid/images/400/200?bottom1"),
            placeReview = "직원분들이 친절하고 공간이 깔끔해서 자주 오고 싶어요.",
            bssReview = "배터리 충전 속도가 빨라서 금방 교체할 수 있었어요.",
            tags = listOf("친절해요", "배터리 빨라요", "깔끔해요"),
            likeCount = 9
        )
    ),
    totalImageCount = 6
)

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun PlaceBottomSheetContentCollapsedPreview() {
    PlaceBottomSheetContent(
        placeData = previewBottomSheetData,
        isExpanded = false,
        onBackClick = {},
        onCloseClick = {},
        onFavoriteClick = {},
        onDepartureClick = {},
        onArrivalClick = {},
        onWriteReviewClick = {},
        onBatterySwapClick = {},
        onMenuViewAllClick = {},
        onReviewViewAllClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PlaceBottomSheetContentExpandedPreview() {
    PlaceBottomSheetContent(
        placeData = previewBottomSheetData,
        isExpanded = true,
        onBackClick = {},
        onCloseClick = {},
        onFavoriteClick = {},
        onDepartureClick = {},
        onArrivalClick = {},
        onWriteReviewClick = {},
        onBatterySwapClick = {},
        onMenuViewAllClick = {},
        onReviewViewAllClick = {}
    )
}
