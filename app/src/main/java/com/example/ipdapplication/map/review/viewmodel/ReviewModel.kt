package com.example.ipdapplication.map.review.viewmodel

import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.map.review.dto.ReviewDto
import com.example.ipdapplication.map.review.dto.TagDto
import com.example.ipdapplication.map.review.dto.TagType
import com.example.ipdapplication.map.review.repository.ReviewRepository
import com.example.ipdapplication.map.review.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewModel @Inject constructor(
    private val repo: ReviewRepository,
    private val tagRepo: TagRepository
): ViewModel() {

    data class UiStateReview(
        val placeId: Long? = null,
        val reviews: List<ReviewDto> = emptyList<ReviewDto>(),
        val tags: List<TagDto> = emptyList(),
        val placeTags: List<TagDto> = emptyList(),
        val bssTags: List<TagDto> = emptyList(),
        val loading: Boolean = false,
        val error: String? = null,
        val toast: String? = null
    )

    private val _state = MutableStateFlow(UiStateReview())
    val state: StateFlow<UiStateReview> = _state.asStateFlow()


    fun loadReviewsForPlace(placeId: Long) {
        _state.update { it.copy(placeId = placeId, loading = true, error = null) }

        viewModelScope.launch {
            runCatching {
                repo.getReviewByPlace(placeId)
            }.onSuccess { list ->
                _state.update {
                    it.copy(
                        reviews = list,
                        loading = false,
                        error = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "리뷰를 불러오지 못했습니다.",
                        toast = "리뷰 불러오기 실패"
                    )
                }
            }
        }
    }

    fun createReview(
        placeId: Long,
        reviewComment: String?,
        bssComment: String?,
        userId: Long,
        tagIds: List<Long>? = null,
        imageUrls: List<String>? = null,
        imageFiles: List<java.io.File> = emptyList()
    ) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            runCatching {
                repo.createReview(
                    placeId = placeId,
                    reviewComment = reviewComment,
                    bssComment = bssComment,
                    tagIds = tagIds,
                    userId = userId,
                    imageUrls = imageUrls,
                    imageFiles = imageFiles
                )
            }.onSuccess { created ->
                _state.update {
                    it.copy(
                        reviews = it.reviews + created,
                        loading = false,
                        toast = "리뷰가 등록되었습니다."
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "리뷰 등록 실패",
                        toast = "리뷸 등록 실패"
                    )
                }
            }
        }
    }

    fun updateReview(
        reviewId: Long,
        placeId: Long,
        reviewComment: String?,
        bssComment: String?,
        userId: Long,
        tagIds: List<Long>? = null,
        imageUrls: List<String>? = null,
        imageFiles: List<java.io.File> = emptyList()
    ) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            runCatching {
                repo.updateReview(
                    reviewId = reviewId,
                    placeId = placeId,
                    reviewComment = reviewComment,
                    bssComment = bssComment,
                    userId = userId,
                    tagIds = tagIds,
                    imageUrls = imageUrls,
                    imageFiles = imageFiles
                )
            }.onSuccess { updated ->
                _state.update { state ->
                    state.copy(
                        reviews = state.reviews.map {
                            if (it.id == updated.id) updated else it
                        },
                        loading = false,
                        toast = "리뷰가 수정되었습니다."
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "리뷰 수정 실패",
                        toast = "리뷰 수정 실패"
                    )
                }
            }
        }
    }

    fun deleteReview(reviewId: Long) {
        val placeId = state.value.placeId

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            runCatching {
                repo.deleteReview(reviewId)
            }.onSuccess {
                _state.update { state ->
                    state.copy(
                        reviews = state.reviews.filterNot { it.id == reviewId },
                        loading = false,
                        toast = "리뷰가 삭제되었습니다."
                    )
                }
                // 필요하면 여기서 placeId 로 다시 전체 로딩도 가능
                // placeId?.let { loadReviewsForPlace(it) }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "리뷰 삭제 실패",
                        toast = "리뷰 삭제 실패"
                    )
                }
            }
        }
    }

    fun clearToast() {
        _state.update { it.copy(toast = null) }
    }

    fun loadTags() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            runCatching {
                tagRepo.getAllTags()
            }.onSuccess { allTags ->
                val placeTags = allTags.filter {
                    it.tagType == TagType.RESTAURANT || it.tagType == TagType.COMMON
                }
                val bssTags = allTags.filter {
                    it.tagType == TagType.BSS || it.tagType == TagType.COMMON
                }

                _state.update {
                    it.copy(
                        tags = allTags,
                        placeTags = placeTags,
                        bssTags = bssTags,
                        loading = false,
                        error = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "태그를 불러오지 못했습니다.",
                        toast = "태그 불러오기 실패"
                    )
                }
            }
        }
    }

}
