package com.example.ipdapplication.cupon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.cupon.api.CouponApi
import com.example.ipdapplication.cupon.dto.CouponData
import com.example.ipdapplication.cupon.dto.toCouponData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

data class CouponUiState(
    val isLoading: Boolean = false,
    val coupons: List<CouponData> = emptyList(),
    val availableCount: Int = 0,
    val errorMessage: String? = null,
    val issuedCoupon: CouponData? = null,  // 방금 발급된 쿠폰
    val issueSuccess: Boolean = false
)

@HiltViewModel
class CouponViewModel @Inject constructor(
    private val couponApi: CouponApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(CouponUiState())
    val uiState: StateFlow<CouponUiState> = _uiState.asStateFlow()

    init {
        loadMyCoupons()
    }

    fun loadMyCoupons(status: String? = null) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val result = couponApi.getMyCoupons(status)
                val mapped = result.map { it.toCouponData() }
                val availableCount = result.count { it.status == "AVAILABLE" }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    coupons = mapped,
                    availableCount = availableCount,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "쿠폰을 불러오지 못했습니다."
                )
            }
        }
    }

    /**
     * BSS 교체 완료 시 쿠폰 발급
     * @param policyId 쿠폰 정책 ID
     * @param source 쿠폰 소스 ("BSS", "GCOO" 등)
     */
    fun issueCoupon(policyId: Long, source: String = "BSS") {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, issueSuccess = false)
        viewModelScope.launch {
            try {
                val result = couponApi.issueCoupon(policyId, source)
                val couponData = result.toCouponData()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    issuedCoupon = couponData,
                    issueSuccess = true,
                    errorMessage = null
                )
                // 쿠폰 목록 새로고침
                loadMyCoupons()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    issueSuccess = false,
                    errorMessage = "쿠폰 발급에 실패했습니다."
                )
            }
        }
    }

    fun clearIssuedCoupon() {
        _uiState.value = _uiState.value.copy(issuedCoupon = null, issueSuccess = false)
    }
}
