package com.example.ipdapplication.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.map.api.DirectionsApi
import com.example.ipdapplication.map.dto.AddressDto
import com.example.ipdapplication.map.dto.CommentDto
import com.example.ipdapplication.map.dto.PlaceCreateRequest
import com.example.ipdapplication.map.dto.PlaceDto
import com.example.ipdapplication.map.dto.PlaceLikeDto
import com.example.ipdapplication.map.gcoo.dto.GcooDto
import com.example.ipdapplication.map.gcoo.repository.GcooRepository
import com.example.ipdapplication.map.repository.AddressRepository
import com.example.ipdapplication.map.repository.CommentRepository
import com.example.ipdapplication.map.repository.PlaceRepository
import com.example.ipdapplication.map.repository.PlaceLikeRepository
import com.example.ipdapplication.map.repository.PlaceCommentLikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val repo: PlaceRepository,
    private val gcooRepo: GcooRepository,
    private val commentRepo: CommentRepository,
    private val directionsApi: DirectionsApi,
    private val placeLikeRepo: PlaceLikeRepository,
    private val placeCommentLikeRepo: PlaceCommentLikeRepository,
    private val addressRepo: AddressRepository
) : ViewModel() {

    data class MapUiState(
        val places: List<PlaceDto> = emptyList(),
        val gcoos: List<GcooDto> = emptyList(),
        val showPlaces: Boolean = true, // ON/OFF 토글 상태 (true = ON = place+gcoo 모두 표시)
        val tempMarker: Pair<Double, Double>? = null,
        val selected: PlaceDto? = null,
        val selectedGcoo: GcooDto? = null,
        val comments: List<CommentDto> = emptyList(),
        // 선택된 place 의 좋아요 상태
        val selectedPlaceLike: PlaceLikeDto? = null,
        // 댓글별 좋아요 상태: key = commentId
        val commentLikeMap: Map<Long, PlaceLikeDto> = emptyMap(),
        val loading: Boolean = false,
        val error: String? = null,
        val toast: String? = null,
        val selectedLocationText: String = ""
    )

    private val _state = MutableStateFlow(MapUiState())
    val state: StateFlow<MapUiState> = _state.asStateFlow()

    fun loadAll() = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repo.getPlaces() }
            .onSuccess { list ->
                _state.update { it.copy(places = list, loading = false) }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "목록 불러오기 실패"
                    )
                }
            }
        // Gcoo 데이터 로드 (더미 데이터)
        loadGcoos()
    }

    fun loadGcoos() = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { gcooRepo.getGcoos() }
            .onSuccess { list ->
                _state.update { it.copy(gcoos = list, loading = false) }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "킥보드 목록 불러오기 실패"
                    )
                }
            }
    }

    fun toggleShowPlaces() {
        _state.update { it.copy(showPlaces = !it.showPlaces) }
    }

    fun selectGcoo(gcoo: GcooDto) {
        _state.update {
            it.copy(
                selectedGcoo = gcoo,
                selected = null, // place 선택 해제
                tempMarker = null,
                comments = emptyList(),
                selectedPlaceLike = null,
                commentLikeMap = emptyMap(),
                selectedLocationText = ""
            )
        }
    }

    fun clearGcooSelection() {
        _state.update { it.copy(selectedGcoo = null) }
    }

    fun setTempMarker(lat: Double, lng: Double) {
        _state.update { it.copy(tempMarker = lat to lng) }
    }

    fun clearTemp() {
        _state.update { it.copy(tempMarker = null) }
    }

    fun selectPlace(p: PlaceDto) {
        _state.update {
            it.copy(
                selected = p,
                tempMarker = null,
                comments = emptyList(),
                selectedPlaceLike = null,
                commentLikeMap = emptyMap(),
                selectedLocationText = ""
            )
        }
        p.id?.let { id ->
            getAllComment(id)
            loadPlaceLikeStatus(id)
        }

        viewModelScope.launch {
            val addr = runCatching {
                // 여기서 suspend 함수 호출
                getAddress(p.latitude, p.longitude)
            }.getOrElse {
                // 예외 발생 시 표시할 문구
                "주소를 불러오는 데 실패했습니다."
            }

            // 주소 로딩이 끝난 뒤 상태 갱신
            _state.update { state ->
                // 아직 같은 place 가 선택되어 있을 때만 업데이트 (안전하게)
                if (state.selected?.id == p.id) {
                    state.copy(selectedLocationText = addr)
                } else {
                    state
                }
            }
        }
    }

    fun clearSelection() {
        _state.update {
            it.copy(
                selected = null,
                selectedGcoo = null,
                comments = emptyList(),
                selectedPlaceLike = null,
                commentLikeMap = emptyMap()
            )
        }
    }

    fun savePlace(
        title: String,
        description: String?,
        telNumber: String?,
        type: String?,
        imageFiles: List<File> = emptyList()
    ) = viewModelScope.launch {
        val pos = state.value.tempMarker
        if (pos == null) {
            _state.update { it.copy(toast = "지도를 탭해서 위치를 먼저 선택하세요.") }
            return@launch
        }
        val (lat, lng) = pos
        val req = PlaceCreateRequest(
            title = title,
            description = description,
            latitude = lat,
            longitude = lng,
            telNumber = telNumber,
            type = type
        )

        _state.update { it.copy(loading = true, error = null) }
        runCatching { repo.createPlace(req, imageFiles) }
            .onSuccess { created ->
                _state.update {
                    it.copy(
                        places = it.places + created,
                        tempMarker = null,
                        loading = false,
                        toast = "저장 완료"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "저장 실패"
                    )
                }
            }
    }

    fun updatePlace(
        id: Long,
        title: String,
        description: String?,
        telNumber: String?,
        type: String?,
        latitude: Double,
        longitude: Double
    ) = viewModelScope.launch {
        val req = PlaceCreateRequest(
            title = title,
            description = description,
            latitude = latitude,
            longitude = longitude,
            telNumber = telNumber,
            type = type
        )

        _state.update { it.copy(loading = true, error = null) }
        runCatching { repo.updatePlace(id, req) }
            .onSuccess { updated ->
                _state.update {
                    it.copy(
                        places = it.places.map { p -> if (p.id == id) updated else p },
                        selected = updated,
                        loading = false,
                        toast = "수정 완료"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "수정 실패"
                    )
                }
            }
    }

    fun deletePlace(id: Long) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repo.deletePlace(id) }
            .onSuccess {
                _state.update {
                    it.copy(
                        places = it.places.filter { p -> p.id != id },
                        selected = null,
                        loading = false,
                        toast = "삭제 완료"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "삭제 실패"
                    )
                }
            }
    }

    // -------------------- place 댓글 ------------------------

    fun addComment(placeId: Long, content: String) = viewModelScope.launch {
        runCatching {
            commentRepo.add(placeId, content)
            getAllComment(placeId)
        }.onFailure { e ->
        }
    }

    fun deleteComment(commentId: Long) = viewModelScope.launch {
        runCatching {
            commentRepo.delete(commentId)
            state.value.selected?.id?.let { getAllComment(it) }
        }.onFailure { e ->
        }
    }

    fun getAllComment(placeId: Long) = viewModelScope.launch {
        runCatching { commentRepo.getAllComment(placeId) }
            .onSuccess { list ->
                _state.update { it.copy(comments = list) }
                // 댓글 목록 갱신 후 댓글 좋아요 상태도 같이 갱신
                loadCommentLikes(list)
            }
            .onFailure { exception ->
                _state.update { it.copy(comments = emptyList(), commentLikeMap = emptyMap()) }
            }
    }

    // -------------------- place 좋아요 ------------------------

    private fun loadPlaceLikeStatus(placeId: Long) = viewModelScope.launch {
        runCatching { placeLikeRepo.getStatus(placeId) }
            .onSuccess { dto ->
                _state.update { it.copy(selectedPlaceLike = dto) }
            }
            .onFailure { e ->
                // API 실패 시 기본값 설정
                _state.update { it.copy(selectedPlaceLike = PlaceLikeDto(liked = false, likeCount = 0)) }
            }
    }

    fun togglePlaceLike() = viewModelScope.launch {
        val placeId = state.value.selected?.id ?: run {
            return@launch
        }
        val currentLike = state.value.selectedPlaceLike

        // 낙관적 업데이트: 즉시 UI 변경
        val newLiked = !(currentLike?.liked ?: false)
        val newCount = (currentLike?.likeCount ?: 0) + if (newLiked) 1 else -1
        val optimisticLike = PlaceLikeDto(
            liked = newLiked,
            likeCount = maxOf(0, newCount) // 음수 방지
        )
        _state.update { it.copy(selectedPlaceLike = optimisticLike) }

        runCatching { placeLikeRepo.toggle(placeId) }
            .onSuccess { dto ->
                _state.update { it.copy(selectedPlaceLike = dto) }
            }
            .onFailure { e ->
                // API 실패해도 로컬 상태 유지 (롤백하지 않음)
            }
    }

    // -------------------- place 댓글 좋아요 ------------------------

    private suspend fun loadCommentLikes(comments: List<CommentDto>) {
        if (comments.isEmpty()) {
            _state.update { it.copy(commentLikeMap = emptyMap()) }
            return
        }

        val map = mutableMapOf<Long, PlaceLikeDto>()
        for (c in comments) {
            val id = c.id ?: continue
            runCatching {
                placeCommentLikeRepo.getStatus(id)
            }.onSuccess { dto ->
                map[id] = dto
            }.onFailure { e ->
            }
        }
        _state.update { it.copy(commentLikeMap = map) }
    }

    fun toggleCommentLike(commentId: Long) = viewModelScope.launch {
        runCatching { placeCommentLikeRepo.toggle(commentId) }
            .onSuccess { dto ->
                _state.update { s ->
                    val newMap = s.commentLikeMap.toMutableMap()
                    newMap[commentId] = dto
                    s.copy(commentLikeMap = newMap)
                }
            }
            .onFailure { e ->
            }
    }

    // ============================================================
    // ================  Directions (경로) 관련 ====================
    // ============================================================

    data class DirectionsPoint(val lat: Double, val lng: Double)
    data class DirectionsUiRoute(
        val distance: Int,
        val duration: Long,
        val path: List<DirectionsPoint>
    )

    private suspend fun getAddress(lat: Double, lng: Double): String {
        val dto = addressRepo.getAddress(lat, lng)
        return dto.toDisplayString()
    }

    private fun AddressDto.toDisplayString(): String {
        return listOf(sido, sigungu, dong, building)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    suspend fun requestRoute(
        startLat: Double,
        startLng: Double,
        goalLat: Double,
        goalLng: Double
    ): DirectionsUiRoute {
        return try {
            val res = directionsApi.getDirections(
                startLat = startLat,
                startLng = startLng,
                goalLat = goalLat,
                goalLng = goalLng
            )


            DirectionsUiRoute(
                distance = res.distance,
                duration = res.duration,
                path = res.path.map { DirectionsPoint(it.lat, it.lng) }
            )
        } catch (e: Exception) {
            throw e
        }
    }
}
