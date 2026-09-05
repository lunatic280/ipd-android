package com.example.ipdapplication.admin.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onBackClick: () -> Unit,
    onPlaceManageClick: () -> Unit,
    onMenuManageClick: () -> Unit,
    onGcooManageClick: () -> Unit,
    onLedControlClick: () -> Unit = {},
    onCouponManageClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "관리자 페이지",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C46E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "관리 메뉴",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Place 관리 카드
            AdminMenuCard(
                title = "Place 등록/관리",
                description = "지도에 새로운 장소를 등록하고 관리합니다",
                icon = Icons.Default.LocationOn,
                onClick = onPlaceManageClick
            )

            // Menu 관리 카드
            AdminMenuCard(
                title = "메뉴 등록/관리",
                description = "장소의 메뉴를 등록하고 관리합니다",
                icon = Icons.Default.Menu,
                onClick = onMenuManageClick
            )

            // Gcoo 관리 카드
            AdminMenuCard(
                title = "킥보드(Gcoo) 관리",
                description = "킥보드를 등록하고 수정/삭제합니다",
                icon = Icons.Default.LocationOn,
                onClick = onGcooManageClick
            )

            // LED 제어 카드
            AdminMenuCard(
                title = "LED 제어",
                description = "장치의 LED를 원격으로 제어합니다",
                icon = Icons.Default.Star,
                onClick = onLedControlClick
            )

            // 쿠폰 관리 카드
            AdminMenuCard(
                title = "쿠폰 발급/관리",
                description = "사용자에게 쿠폰을 발급하고 관리합니다",
                icon = Icons.Default.Star,
                onClick = onCouponManageClick
            )
        }
    }
}

@Composable
fun AdminMenuCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE8F5E9),
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color(0xFF00C46E),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminScreenPreview() {
    AdminScreen(
        onBackClick = {},
        onPlaceManageClick = {},
        onMenuManageClick = {},
        onGcooManageClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AdminMenuCardPreview() {
    AdminMenuCard(
        title = "Place 등록/관리",
        description = "지도에 새로운 장소를 등록하고 관리합니다",
        icon = Icons.Default.LocationOn,
        onClick = {}
    )
}
