package com.example.ipdapplication.map.screen.review

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.map.review.dto.TagDto
import com.example.ipdapplication.map.review.dto.TagType
import com.example.ipdapplication.map.review.viewmodel.ReviewModel

/**
 * 리뷰 작성 화면 전체 컴포저블
 */
@Composable
fun ReviewWriteScreen(
    placeName: String,
    visitCount: Int,
    onClose: () -> Unit,
    onSubmit: (ReviewWriteData) -> Unit,
    reviewViewModel: ReviewModel = hiltViewModel()
) {
    val reviewState by reviewViewModel.state.collectAsState()

    // 태그 로드
    LaunchedEffect(Unit) {
        reviewViewModel.loadTags()
    }

    var placeTagIdsSelected by remember { mutableStateOf(setOf<Long>()) }
    var bssTagIdsSelected by remember { mutableStateOf(setOf<Long>()) }
    var placeReviewText by remember { mutableStateOf("") }
    var bssReviewText by remember { mutableStateOf("") }
    val photoUris = remember { mutableStateListOf<Uri>() }

    // 갤러리에서 이미지 선택하는 launcher (여러 장 선택 허용)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            uris.filter { it !in photoUris }.forEach { photoUris.add(it) }
        }
    }

    ReviewWriteScreenContent(
        placeName = placeName,
        visitCount = visitCount,
        placeTags = reviewState.placeTags,
        bssTags = reviewState.bssTags,
        placeTagIdsSelected = placeTagIdsSelected,
        bssTagIdsSelected = bssTagIdsSelected,
        placeReviewText = placeReviewText,
        bssReviewText = bssReviewText,
        photoUris = photoUris,
        onPlaceTagToggle = { tagId ->
            placeTagIdsSelected = if (placeTagIdsSelected.contains(tagId)) {
                placeTagIdsSelected - tagId
            } else {
                placeTagIdsSelected + tagId
            }
        },
        onBssTagToggle = { tagId ->
            bssTagIdsSelected = if (bssTagIdsSelected.contains(tagId)) {
                bssTagIdsSelected - tagId
            } else {
                bssTagIdsSelected + tagId
            }
        },
        onPlaceReviewChange = { placeReviewText = it },
        onBssReviewChange = { bssReviewText = it },
        onPhotoClick = { imagePickerLauncher.launch("image/*") },
        onPhotoRemove = { uri -> photoUris.remove(uri) },
        onClose = onClose,
        onSubmit = {
            onSubmit(
                ReviewWriteData(
                    placeTagIds = placeTagIdsSelected.toList(),
                    bssTagIds = bssTagIdsSelected.toList(),
                    placeReview = placeReviewText,
                    bssReview = bssReviewText,
                    photoUris = photoUris.toList()
                )
            )
        }
    )
}

/**
 * 리뷰 작성 화면 콘텐츠 (상태를 외부에서 주입)
 */
@Composable
fun ReviewWriteScreenContent(
    placeName: String,
    visitCount: Int,
    placeTags: List<TagDto>,
    bssTags: List<TagDto>,
    placeTagIdsSelected: Set<Long>,
    bssTagIdsSelected: Set<Long>,
    placeReviewText: String,
    bssReviewText: String,
    photoUris: List<Uri>,
    onPlaceTagToggle: (Long) -> Unit,
    onBssTagToggle: (Long) -> Unit,
    onPlaceReviewChange: (String) -> Unit,
    onBssReviewChange: (String) -> Unit,
    onPhotoClick: () -> Unit,
    onPhotoRemove: (Uri) -> Unit,
    onClose: () -> Unit,
    onSubmit: () -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // 헤더 섹션
            ReviewWriteHeader(
                placeName = placeName,
                visitCount = visitCount,
                onClose = onClose
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 스크롤 가능한 콘텐츠 영역
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                // 질문 제목
                Text(
                    text = "지쿠 플레이스 & BSS는 어땠나요?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 지쿠 플레이스는 태그 선택
                TagSelectionSectionWithIds(
                    label = "지쿠 플레이스는",
                    tags = placeTags,
                    selectedTagIds = placeTagIdsSelected,
                    onTagToggle = onPlaceTagToggle
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 지쿠 BSS는 태그 선택
                TagSelectionSectionWithIds(
                    label = "지쿠 BSS는",
                    tags = bssTags,
                    selectedTagIds = bssTagIdsSelected,
                    onTagToggle = onBssTagToggle
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 진짜 사진 추가 섹션
                PhotoAddSection(
                    photoUris = photoUris,
                    onPhotoClick = onPhotoClick,
                    onPhotoRemove = onPhotoRemove
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 리뷰를 작성해 주세요!
                Text(
                    text = "리뷰를 작성해 주세요!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 지쿠 플레이스는 텍스트 입력
                ReviewTextInputSection(
                    label = "지쿠 플레이스는",
                    value = placeReviewText,
                    placeholder = "유명한 맛집이라길래 궁금했는데 진짜 맛있어용",
                    onValueChange = onPlaceReviewChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 지쿠 BSS는 텍스트 입력
                ReviewTextInputSection(
                    label = "지쿠 BSS는",
                    value = bssReviewText,
                    placeholder = "",
                    onValueChange = onBssReviewChange
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 등록 버튼
            SubmitButton(
                onClick = onSubmit
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ==================== Preview Functions ====================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReviewWriteScreenPreview() {
    val samplePlaceTags = listOf(
        TagDto(1L, "맛있어요", TagType.RESTAURANT),
        TagDto(2L, "양이 많아요", TagType.RESTAURANT),
        TagDto(3L, "간이 적당해요", TagType.COMMON),
        TagDto(4L, "사이드가 맛있어요", TagType.RESTAURANT)
    )
    val sampleBssTags = listOf(
        TagDto(5L, "배터리가 많아요", TagType.BSS),
        TagDto(6L, "찾기 쉬워요", TagType.COMMON),
        TagDto(7L, "재밌어요", TagType.BSS),
        TagDto(8L, "교체가 편리해요", TagType.BSS)
    )

    ReviewWriteScreenContent(
        placeName = "심가네 떡볶이",
        visitCount = 2,
        placeTags = samplePlaceTags,
        bssTags = sampleBssTags,
        placeTagIdsSelected = setOf(1L, 3L),
        bssTagIdsSelected = setOf(6L, 7L),
        placeReviewText = "유명한 맛집이라길래 궁금했는데 진짜 맛있어용",
        bssReviewText = "",
        photoUris = emptyList(),
        onPlaceTagToggle = {},
        onBssTagToggle = {},
        onPlaceReviewChange = {},
        onBssReviewChange = {},
        onPhotoClick = {},
        onPhotoRemove = { _ -> },
        onClose = {},
        onSubmit = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReviewWriteScreenEmptyPreview() {
    val samplePlaceTags = listOf(
        TagDto(1L, "맛있어요", TagType.RESTAURANT),
        TagDto(2L, "양이 많아요", TagType.RESTAURANT),
        TagDto(3L, "간이 적당해요", TagType.COMMON),
        TagDto(4L, "사이드가 맛있어요", TagType.RESTAURANT)
    )
    val sampleBssTags = listOf(
        TagDto(5L, "배터리가 많아요", TagType.BSS),
        TagDto(6L, "찾기 쉬워요", TagType.COMMON),
        TagDto(7L, "재밌어요", TagType.BSS),
        TagDto(8L, "교체가 편리해요", TagType.BSS)
    )

    ReviewWriteScreenContent(
        placeName = "심가네 떡볶이",
        visitCount = 2,
        placeTags = samplePlaceTags,
        bssTags = sampleBssTags,
        placeTagIdsSelected = emptySet(),
        bssTagIdsSelected = emptySet(),
        placeReviewText = "",
        bssReviewText = "",
        photoUris = emptyList(),
        onPlaceTagToggle = {},
        onBssTagToggle = {},
        onPlaceReviewChange = {},
        onBssReviewChange = {},
        onPhotoClick = {},
        onPhotoRemove = { _ -> },
        onClose = {},
        onSubmit = {}
    )
}
