package com.example.ipdapplication.cupon.screen

import CouponCard
import TabItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ipdapplication.cupon.dto.CouponData
import com.example.ipdapplication.cupon.viewmodel.CouponUiState
import com.example.ipdapplication.cupon.viewmodel.CouponViewModel

private val defaultCoupons = listOf(
    CouponData("지쿠반가", "2,000원", "배달비 아끼는 쿠폰", "지쿠"),
    CouponData("BSS 배달 지원", "2,000원", "배송비 지원 쿠폰", "BSS"),
    CouponData("BSS 할인", "1,000원", "친구 추천 전용", "BSS"),
    CouponData("배달교체", "20%", "주문 금액 20% 할인", "지쿠")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuponScreen(
    viewModel: CouponViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CuponScreenContent(
        uiState = uiState,
        onReload = { viewModel.loadMyCoupons() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuponScreenContent(
    uiState: CouponUiState,
    onReload: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: 보유 쿠폰, 1: 만료 쿠폰
    var couponCode by remember { mutableStateOf("") }
    var selectedCouponType by remember { mutableStateOf<Int?>(null) } // null: 전체, 0: 지쿠 1: BSS

    val coupons = if (uiState.coupons.isNotEmpty()) uiState.coupons else defaultCoupons

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "쿠폰/포인트",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onReload) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "새로고침"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TabItem(
                        text = "보유 쿠폰",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    TabItem(
                        text = "만료 쿠폰",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponCode,
                        onValueChange = { couponCode = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("쿠폰 코드 입력") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = Color.Gray
                        )
                    )
                    Button(
                        onClick = { /* no-op for preview */ },
                        modifier = Modifier.height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text("적용하기")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            selectedCouponType = if (selectedCouponType == 0) null else 0
                        }
                    ) {
                        RadioButton(
                            selected = selectedCouponType == 0,
                            onClick = {
                                selectedCouponType = if (selectedCouponType == 0) null else 0
                            }
                        )
                        Text(text = "지쿠 쿠폰")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            selectedCouponType = if (selectedCouponType == 1) null else 1
                        }
                    ) {
                        RadioButton(
                            selected = selectedCouponType == 1,
                            onClick = {
                                selectedCouponType = if (selectedCouponType == 1) null else 1
                            }
                        )
                        Text(text = "BSS 쿠폰")
                    }
                }

                val filteredCoupons = coupons.filter {
                    when (selectedCouponType) {
                        null -> true
                        0 -> it.source == "지쿠"
                        1 -> it.source == "BSS"
                        else -> false
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredCoupons) { coupon ->
                        CouponCard(coupon = coupon)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CuponScreenPreview() {
    CuponScreenContent(
        uiState = CouponUiState(coupons = defaultCoupons),
        onReload = {}
    )
}
