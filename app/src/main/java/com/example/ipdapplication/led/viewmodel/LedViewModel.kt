package com.example.ipdapplication.led.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.led.api.LedApi
import com.example.ipdapplication.led.dto.LedStatusDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedViewModel @Inject constructor(
    private val ledApi: LedApi
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    fun changeLedColor(deviceId: String, color: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                _successMessage.value = null

                val dto = LedStatusDto(color = color)
                ledApi.changeLed(deviceId, dto)

                _successMessage.value = when (color) {
                    "OFF" -> "LED가 꺼졌습니다."
                    else -> "LED 색상이 ${color}(으)로 변경되었습니다."
                }
            } catch (e: Exception) {
                _errorMessage.value = "LED 제어 실패: 요청을 처리하지 못했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
