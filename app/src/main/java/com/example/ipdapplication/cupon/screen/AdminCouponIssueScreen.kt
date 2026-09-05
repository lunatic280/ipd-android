package com.example.ipdapplication.cupon.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.cupon.viewmodel.AdminCouponIssueUiState
import com.example.ipdapplication.cupon.viewmodel.AdminCouponIssueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCouponIssueScreen(
    viewModel: AdminCouponIssueViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState = viewModel.uiState

    AdminCouponIssueContent(
        uiState = uiState,
        onUserIdChange = viewModel::onUserIdChange,
        onNameChange = viewModel::onNameChange,
        onDiscountAmountChange = viewModel::onDiscountAmountChange,
        onMinOrderPriceChange = viewModel::onMinOrderPriceChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onSubmit = viewModel::issue,
        onBackClick = onBackClick,
        onReset = {
            viewModel.onUserIdChange("")
            viewModel.onNameChange("")
            viewModel.onDiscountAmountChange("")
            viewModel.onMinOrderPriceChange("")
            viewModel.onDescriptionChange("")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCouponIssueContent(
    uiState: AdminCouponIssueUiState,
    onUserIdChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDiscountAmountChange: (String) -> Unit,
    onMinOrderPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBackClick: () -> Unit = {},
    onReset: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("관리자 쿠폰 발급") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = onReset) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "입력 초기화")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.userIdInput,
                onValueChange = onUserIdChange,
                label = { Text("사용자 ID") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text("쿠폰 이름") },
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.discountAmountInput,
                onValueChange = onDiscountAmountChange,
                label = { Text("할인 금액 (원)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = uiState.minOrderPriceInput,
                onValueChange = onMinOrderPriceChange,
                label = { Text("최소 주문 금액") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                label = { Text("설명") }
            )

            Button(
                onClick = onSubmit,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isLoading) "발급 중.." else "쿠폰 생성 + 발급")
            }

            if (uiState.successMessage != null) {
                Text(uiState.successMessage, color = MaterialTheme.colorScheme.primary)
            }
            if (uiState.errorMessage != null) {
                Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminCouponIssuePreview() {
    var state by remember {
        mutableStateOf(
            AdminCouponIssueUiState(
                userIdInput = "123",
                name = "미리보기 쿠폰",
                discountAmountInput = "3000",
                minOrderPriceInput = "10000",
                description = "배송비 절약 쿠폰"
            )
        )
    }
    AdminCouponIssueContent(
        uiState = state,
        onUserIdChange = { state = state.copy(userIdInput = it) },
        onNameChange = { state = state.copy(name = it) },
        onDiscountAmountChange = { state = state.copy(discountAmountInput = it) },
        onMinOrderPriceChange = { state = state.copy(minOrderPriceInput = it) },
        onDescriptionChange = { state = state.copy(description = it) },
        onSubmit = {},
        onBackClick = {},
        onReset = { state = AdminCouponIssueUiState() }
    )
}
