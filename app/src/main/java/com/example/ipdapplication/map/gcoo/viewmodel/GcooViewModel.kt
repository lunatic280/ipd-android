package com.example.ipdapplication.map.gcoo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.map.gcoo.dto.GcooDto
import com.example.ipdapplication.map.gcoo.repository.GcooRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GcooViewModel @Inject constructor(
    private val repository: GcooRepository
) : ViewModel() {

    data class GcooUiState(
        val gcoos: List<GcooDto> = emptyList(),
        val selectedGcoo: GcooDto? = null,
        val tempMarker: Pair<Double, Double>? = null,  // 임시 마커 위치 (위도, 경도)
        val loading: Boolean = false,
        val error: String? = null,
        val toast: String? = null
    )

    private val _state = MutableStateFlow(GcooUiState())
    val state: StateFlow<GcooUiState> = _state.asStateFlow()

    /**
     * 전체 Gcoo 목록 로드
     */
    fun loadAll() = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repository.getGcoos() }
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

    /**
     * 특정 Gcoo 조회
     */
    fun getGcooById(gcooId: Long) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repository.getGcooById(gcooId) }
            .onSuccess { gcoo ->
                _state.update {
                    it.copy(
                        selectedGcoo = gcoo,
                        loading = false
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "킥보드 정보 불러오기 실패"
                    )
                }
            }
    }

    /**
     * Gcoo 생성 (OWNER만 가능)
     */
    fun createGcoo(
        charge: Int,
        distance: Int,
        subTitle: String,
        description: String,
        latitude: Double,
        longitude: Double,
        userId: Long,
        imageFile: File?
    ) = viewModelScope.launch {
        val dto = GcooDto(
            charge = charge,
            distance = distance,
            subTitle = subTitle,
            description = description,
            latitude = latitude,
            longitude = longitude,
            userId = userId
        )

        _state.update { it.copy(loading = true, error = null) }
        runCatching { repository.createGcoo(dto, imageFile) }
            .onSuccess { created ->
                _state.update {
                    it.copy(
                        gcoos = it.gcoos + created,
                        loading = false,
                        toast = "킥보드 등록 완료"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "킥보드 등록 실패"
                    )
                }
            }
    }

    /**
     * Gcoo 수정 (OWNER만 가능)
     */
    fun updateGcoo(
        gcooId: Long,
        charge: Int,
        distance: Int,
        subTitle: String,
        description: String,
        latitude: Double,
        longitude: Double,
        userId: Long,
        imageFile: File?
    ) = viewModelScope.launch {
        val dto = GcooDto(
            id = gcooId,
            charge = charge,
            distance = distance,
            subTitle = subTitle,
            description = description,
            latitude = latitude,
            longitude = longitude,
            userId = userId
        )

        _state.update { it.copy(loading = true, error = null) }
        runCatching { repository.updateGcoo(gcooId, dto, imageFile) }
            .onSuccess { updated ->
                _state.update {
                    it.copy(
                        gcoos = it.gcoos.map { gcoo ->
                            if (gcoo.id == gcooId) updated else gcoo
                        },
                        loading = false,
                        toast = "킥보드 수정 완료"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "킥보드 수정 실패"
                    )
                }
            }
    }

    /**
     * Gcoo 삭제 (OWNER만 가능)
     */
    fun deleteGcoo(gcooId: Long) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repository.deleteGcoo(gcooId) }
            .onSuccess {
                _state.update {
                    it.copy(
                        gcoos = it.gcoos.filter { gcoo -> gcoo.id != gcooId },
                        loading = false,
                        toast = "킥보드 삭제 완료"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "킥보드 삭제 실패"
                    )
                }
            }
    }

    /**
     * 임시 마커 설정
     */
    fun setTempMarker(lat: Double, lng: Double) {
        _state.update { it.copy(tempMarker = lat to lng) }
    }

    /**
     * 임시 마커 제거
     */
    fun clearTempMarker() {
        _state.update { it.copy(tempMarker = null) }
    }

    /**
     * Toast 메시지 초기화
     */
    fun clearToast() {
        _state.update { it.copy(toast = null) }
    }
}
