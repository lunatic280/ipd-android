package com.example.ipdapplication.map.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.map.menu.dto.MenuDto
import com.example.ipdapplication.map.menu.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repo: MenuRepository
) : ViewModel() {

    data class UiStateMenu(
        val menus: List<MenuDto> = emptyList(),
        val loading: Boolean = false,
        val error: String? = null,
        val toast: String? = null,
        val placeId: Long? = null
    )

    private val _state = MutableStateFlow(UiStateMenu())
    val state: StateFlow<UiStateMenu> = _state.asStateFlow()

    // 전체 메뉴 조회
    fun loadAllMenus() = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null, placeId = null) }
        runCatching { repo.getMenus() }
            .onSuccess { list ->
                _state.update { it.copy(menus = list, loading = false) }
            }
            .onFailure { exception ->
                _state.update { it.copy(loading = false, error = "요청을 처리하지 못했습니다.") }
            }
    }

    // 특정 Place의 메뉴만 조회 (클라이언트 필터링)
    fun loadMenusByPlaceId(placeId: Long) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null, placeId = placeId) }
        runCatching { repo.getMenusByPlaceId(placeId) }
            .onSuccess { list ->
                _state.update { it.copy(menus = list, loading = false) }
            }
            .onFailure { exception ->
                _state.update { it.copy(loading = false, error = "요청을 처리하지 못했습니다.") }
            }
    }

    fun createMenu(
        placeId: Long,
        name: String,
        price: Int,
        description: String,
        imageFile: File
    ) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repo.createMenu(placeId, name, price, description, imageFile) }
            .onSuccess { created ->
                _state.update {
                    it.copy(
                        menus = it.menus + created,
                        loading = false,
                        toast = "메뉴가 등록되었습니다"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "메뉴 등록 실패: 요청을 처리하지 못했습니다."
                    )
                }
            }
    }

    fun updateMenu(
        menuId: Long,
        placeId: Long,
        name: String,
        price: Int,
        description: String,
        imageFile: File?
    ) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repo.updateMenu(menuId, placeId, name, price, description, imageFile) }
            .onSuccess { updated ->
                _state.update {
                    it.copy(
                        menus = it.menus.map { menu -> if (menu.id == menuId) updated else menu },
                        loading = false,
                        toast = "메뉴가 수정되었습니다"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "메뉴 수정 실패: 요청을 처리하지 못했습니다."
                    )
                }
            }
    }

    fun clearToast() {
        _state.update { it.copy(toast = null) }
    }

    fun deleteMenu(menuId: Long) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching { repo.deleteMenu(menuId) }
            .onSuccess {
                _state.update {
                    it.copy(
                        menus = it.menus.filter { menu -> menu.id != menuId },
                        loading = false,
                        toast = "메뉴가 삭제되었습니다"
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "요청을 처리하지 못했습니다.",
                        toast = "메뉴 삭제 실패: 요청을 처리하지 못했습니다."
                    )
                }
            }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun clearMenus() {
        _state.value = UiStateMenu()
    }
}
