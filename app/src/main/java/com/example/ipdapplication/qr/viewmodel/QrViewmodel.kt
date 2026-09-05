package com.example.ipdapplication.qr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.cupon.api.CouponApi
import com.example.ipdapplication.cupon.dto.CouponData
import com.example.ipdapplication.cupon.dto.toCouponData
import com.example.ipdapplication.qr.repository.QrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrScanViewModel @Inject constructor(
    private val repository: QrRepository,
    private val couponApi: CouponApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<QrScanUiState>(QrScanUiState.Scanning)
    val uiState: StateFlow<QrScanUiState> = _uiState.asStateFlow()

    // 발급된 쿠폰 정보
    private val _issuedCoupon = MutableStateFlow<CouponData?>(null)
    val issuedCoupon: StateFlow<CouponData?> = _issuedCoupon.asStateFlow()

    private var currentSlotNumber: String = ""

    companion object {
        // BSS 교체 완료 시 발급할 쿠폰 정책 ID (서버에서 설정된 값)
        const val BSS_COUPON_POLICY_ID = 1L
    }

    fun onQrScanned(qrData: String) {
        // 이미 스캔 완료 상태면 무시 (중복 스캔 방지)
        if (_uiState.value !is QrScanUiState.Scanning) return

        // QR 데이터에서 슬롯 번호 추출 (전시용 예: "DEMO_BSS : DEMO_SLOT")
        currentSlotNumber = parseSlotNumber(qrData)

        // 첫 번째 단계 진행 중 상태로 전환
        _uiState.value = QrScanUiState.FirstStepInProgress(
            qrData = qrData,
            slotNumber = currentSlotNumber
        )

        // 서버에 openBss 요청
        viewModelScope.launch {
            repository.sendQrData(qrData)
        }
    }

    fun onFirstStepComplete() {
        val currentState = _uiState.value
        if (currentState is QrScanUiState.FirstStepInProgress) {
            // 서버에 nextStep 요청 (첫 번째 모터 작동)
            viewModelScope.launch {
                repository.sendNextStep(currentState.qrData)
            }

            // 두 번째 단계 진행 중 상태로 전환
            _uiState.value = QrScanUiState.SecondStepInProgress(
                qrData = currentState.qrData,
                slotNumber = currentState.slotNumber
            )
        }
    }

    fun onSecondStepComplete() {
        val currentState = _uiState.value
        if (currentState is QrScanUiState.SecondStepInProgress) {
            // 서버에 nextStep 요청 (모터 작동)
            viewModelScope.launch {
                repository.sendNextStep(currentState.qrData)
            }

            // 쿠폰 발급 요청
            issueBssCoupon()

            // 완료 상태로 전환
            _uiState.value = QrScanUiState.SwapComplete(
                qrData = currentState.qrData,
                slotNumber = currentState.slotNumber
            )
        }
    }

    /**
     * BSS 교체 완료 시 쿠폰 발급
     */
    private fun issueBssCoupon() {
        viewModelScope.launch {
            try {
                val response = couponApi.issueCoupon(
                    policyId = BSS_COUPON_POLICY_ID,
                    source = "BSS"
                )
                _issuedCoupon.value = response.toCouponData()
            } catch (e: Exception) {
                // 쿠폰 발급 실패해도 배터리 교체는 완료된 것으로 처리
                // 로그만 남기고 에러 무시
            }
        }
    }

    // 기존 호환성을 위한 메서드
    fun onSwapComplete() {
        onSecondStepComplete()
    }

    fun resetToScanning() {
        _uiState.value = QrScanUiState.Scanning
        _issuedCoupon.value = null
        currentSlotNumber = ""
    }

    private fun parseSlotNumber(qrData: String): String {
        // QR 데이터 형식에 따라 슬롯 번호를 파싱
        // 기본값으로 QR 데이터 일부를 사용
        return if (qrData.contains(":")) {
            qrData
        } else {
            "BSS (S1-E) : ${qrData.takeLast(5)}"
        }
    }
}

sealed interface QrScanUiState {
    object Scanning : QrScanUiState

    // 첫 번째 단계 (openBss 호출)
    data class FirstStepInProgress(
        val qrData: String,
        val slotNumber: String
    ) : QrScanUiState

    // 두 번째 단계 (nextStep 호출)
    data class SecondStepInProgress(
        val qrData: String,
        val slotNumber: String
    ) : QrScanUiState

    // 교체 완료
    data class SwapComplete(
        val qrData: String,
        val slotNumber: String
    ) : QrScanUiState

    // 기존 호환성을 위한 상태들
    data class SwapInProgress(
        val qrData: String,
        val slotNumber: String
    ) : QrScanUiState

    data class Success(val data: String) : QrScanUiState
}
