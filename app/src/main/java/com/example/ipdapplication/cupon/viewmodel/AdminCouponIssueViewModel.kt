package com.example.ipdapplication.cupon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.cupon.api.CouponApi
import com.example.ipdapplication.cupon.dto.AdminCreateAndIssueCouponRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class AdminCouponIssueUiState(
    val userIdInput: String = "",
    val name: String = "",
    val discountAmountInput: String = "",
    val minOrderPriceInput: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AdminCouponIssueViewModel @Inject constructor(
    private val couponApi: CouponApi
) : ViewModel() {

    var uiState: AdminCouponIssueUiState = AdminCouponIssueUiState()
        private set

    fun onUserIdChange(value: String) {
        uiState = uiState.copy(userIdInput = value)
    }

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value)
    }

    fun onDiscountAmountChange(value: String) {
        uiState = uiState.copy(discountAmountInput = value)
    }

    fun onMinOrderPriceChange(value: String) {
        uiState = uiState.copy(minOrderPriceInput = value)
    }

    fun onDescriptionChange(value: String) {
        uiState = uiState.copy(description = value)
    }

    fun issue() {
        val userId = uiState.userIdInput.toLongOrNull()
        val discountAmount = uiState.discountAmountInput.toIntOrNull()
        val minOrderPrice = uiState.minOrderPriceInput.toIntOrNull() ?: 0

        if (userId == null || discountAmount == null || uiState.name.isBlank()) {
            uiState = uiState.copy(
                errorMessage = "유저 ID, 쿠폰 이름, 정액 할인은 필수입니다.",
                successMessage = null
            )
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)

        viewModelScope.launch {
            try {
                val req = AdminCreateAndIssueCouponRequest(
                    userId = userId,
                    name = uiState.name,
                    discountAmount = discountAmount,
                    discountRate = null,
                    minOrderPrice = minOrderPrice,
                    description = uiState.description
                )
                val result = couponApi.adminCreateAndIssue(req)
                uiState = uiState.copy(
                    isLoading = false,
                    successMessage = "쿠폰 발급 완료: ${result.name}",
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "발급 실패",
                    successMessage = null
                )
            }
        }
    }
}
