package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ipdapplication.BuildConfig
import com.example.ipdapplication.map.dto.CommentDto
import com.example.ipdapplication.map.dto.PlaceDto
import com.example.ipdapplication.map.dto.PlaceLikeDto
import com.example.ipdapplication.map.menu.dto.MenuDto
import com.example.ipdapplication.map.review.dto.ReviewDto
import com.example.ipdapplication.ui.theme.IpdApplicationTheme

val BASE_URL = BuildConfig.API_BASE_URL.removeSuffix("/")

val tagIdToLabel = mapOf(
    1L to "맛있어요",
    2L to "양이 많아요",
    3L to "간이 적당해요",
    4L to "사이드가 맛있어요",
    5L to "배터리가 많아요",
    6L to "찾기 쉬워요",
    7L to "재밌어요",
    8L to "교체가 편리해요"
)
fun String?.asFullUrl(): String? {
    val raw = this?.trim().orEmpty()
    if (raw.isEmpty()) return null
    return if (raw.startsWith("http://") || raw.startsWith("https://")) {
        raw
    } else {
        val normalized = if (raw.startsWith("/")) raw else "/$raw"
        "$BASE_URL$normalized"
    }
}

fun List<String>?.asFullUrls(): List<String> =
    this.orEmpty().mapNotNull { it.asFullUrl() }

private fun resolveReviewTags(review: ReviewDto): List<String> {
    val names = review.tags ?: review.tagNames
    if (!names.isNullOrEmpty()) return names

    val fromIds = review.tagIds?.mapNotNull { id ->
        tagIdToLabel[id] ?: "#$id"
    }.orEmpty()

    return fromIds
}

@Composable
fun PlaceDetailSheet(
    place: PlaceDto,
    comments: List<CommentDto>,
    isExpanded: Boolean,
    scrollState: ScrollState,
    placeLike: PlaceLikeDto?,
    commentLikeMap: Map<Long, PlaceLikeDto>,
    onTogglePlaceLike: () -> Unit,
    onToggleCommentLike: (Long) -> Unit,
    location: String,
    menus: List<MenuDto> = emptyList(),
    reviews: List<ReviewDto> = emptyList(),
    onWriteReviewClick: () -> Unit = {},
    onMenuViewAllClick: () -> Unit = {},
    onReviewViewAllClick: () -> Unit = {},
    onDepartureClick: () -> Unit = {},
    onArrivalClick: () -> Unit = {}
) {
    val menuItems = menus.map { menu ->
        MenuItem(
            name = menu.name,
            description = menu.description,
            price = menu.price,
            imageUrl = menu.imageUrl.asFullUrl()
        )
    }

    val reviewItems = reviews.map { review ->
        // imageUrl이 이제 List<String>? 타입
        val allImages = (review.imageUrls.orEmpty() + review.imageUrl.orEmpty()).asFullUrls()
        ReviewItem(
            id = review.id ?: 0L,
            userName = "사용자${review.userId}",
            userGrade = "SILVER 등급",
            userGradeColor = Color.Gray,
            imageUrl = allImages.firstOrNull(),
            imageUrls = allImages,
            placeReview = review.reviewComment ?: "",
            bssReview = review.bssComment ?: "",
            tags = resolveReviewTags(review),
            likeCount = 0
        )
    }

    val imageList = buildList {
        addAll(place.imageUrls)
        addAll(place.images.orEmpty())
        place.imageUrl?.let { add(it) }
        place.mainImageUrl?.let { add(it) }
    }.asFullUrls().distinct()

    // 모든 리뷰에서 가장 많이 사용된 태그 찾기
    val allTags = reviewItems.flatMap { it.tags }
    val tagCounts = allTags.groupingBy { it }.eachCount()
    val sortedTags = tagCounts.entries.sortedByDescending { it.value }
    val topTag = sortedTags.firstOrNull()?.key ?: "데이터 없음"
    val topTagCount = sortedTags.firstOrNull()?.value ?: 0
    val secondTag = sortedTags.getOrNull(1)?.key ?: "데이터 없음"
    val secondTagCount = sortedTags.getOrNull(1)?.value ?: 0

    val totalReviews = reviewItems.size.coerceAtLeast(1)
    val placeProgress = if (topTagCount > 0) topTagCount.toFloat() / totalReviews else 0f
    val bssProgress = if (secondTagCount > 0) secondTagCount.toFloat() / totalReviews else 0f

    val placeData = PlaceDetailData(
        id = place.id ?: 0L,
        name = place.title,
        batteryCount = 2,
        isFavorite = placeLike?.liked ?: false,
        address = location,
        businessStatus = place.type ?: "영업 상태 정보 없음",
        lastOrderTime = null,
        phoneNumber = place.telNumber ?: "전화번호 정보 없음",
        menuItems = menuItems,
        reviewSummary = ReviewSummary(
            placeLabel = "지점 리뷰",
            placeTopTag = topTag,
            placeProgress = placeProgress,
            bssLabel = "BSS 상태",
            bssTopTag = secondTag,
            bssProgress = bssProgress
        ),
        reviews = reviewItems,
        mainImageUrl = imageList.firstOrNull(),
        subImageUrls = if (imageList.size > 1) imageList.drop(1) else emptyList(),
        totalImageCount = imageList.size,
        likeCount = placeLike?.likeCount?.toInt() ?: place.likeCount
    )

    PlaceBottomSheetContent(
        placeData = placeData,
        isExpanded = isExpanded,
        onBackClick = { },
        onCloseClick = { },
        onFavoriteClick = onTogglePlaceLike,
        onDepartureClick = onDepartureClick,
        onArrivalClick = onArrivalClick,
        onWriteReviewClick = onWriteReviewClick,
        onBatterySwapClick = { },
        onMenuViewAllClick = onMenuViewAllClick,
        onReviewViewAllClick = onReviewViewAllClick
    )
}

@Composable
private fun PlaceDetailSheetPreviewContent(isExpanded: Boolean) {
    val dummyPlace = PlaceDto(
        id = 1L,
        title = "테스트 맛집",
        description = "테스트 메뉴가 맛있어요",
        latitude = 0.0,
        longitude = 0.0,
        imageUrls = listOf(
            "https://example.invalid/images/400/200?1",
            "https://example.invalid/images/400/200?2"
        ),
        createdAt = "2025-12-04T12:00:00",
        telNumber = "000-0000-0000",
        type = "영업 중"
    )

    val dummyComments = listOf(
        CommentDto(
            id = 1L,
            placeId = 1L,
            content = "분위기가 좋고 맛있어요!"
        ),
        CommentDto(
            id = 2L,
            placeId = 1L,
            content = "가격도 적당하고 바닥까지 깨끗해서 좋았어요."
        )
    )

    val scrollStatePreview = rememberScrollState()
    val placeLike = PlaceLikeDto(liked = true, likeCount = 42L)
    val commentLikeMap: Map<Long, PlaceLikeDto> = mapOf(
        1L to PlaceLikeDto(liked = true, likeCount = 5L),
        2L to PlaceLikeDto(liked = false, likeCount = 0L)
    )

    IpdApplicationTheme {
        PlaceDetailSheet(
            place = dummyPlace,
            comments = dummyComments,
            isExpanded = isExpanded,
            scrollState = scrollStatePreview,
            placeLike = placeLike,
            commentLikeMap = commentLikeMap,
            onTogglePlaceLike = {},
            onToggleCommentLike = {},
            location = "전시용 가상 주소",
            menus = emptyList(),
            reviews = emptyList()
        )
    }
}

@Preview(showBackground = true, name = "Expanded")
@Composable
fun PlaceDetailSheetPreview() {
    PlaceDetailSheetPreviewContent(isExpanded = true)
}

@Preview(showBackground = true, name = "Collapsed", heightDp = 400)
@Composable
fun PlaceDetailSheetCollapsedPreview() {
    PlaceDetailSheetPreviewContent(isExpanded = false)
}
