package com.example.ipdapplication.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onOpenMap: () -> Unit,
    onOpenCommunity: () -> Unit,
    onOpenCoupon: () -> Unit,
    onOpenQR: () -> Unit,
    onOpenAdmin: () -> Unit = {}
) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.weight(1f)) {
            // 좌측 상단: 쿠폰 / 패스
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(8.dp)
                    .clickable(onClick = onOpenCoupon),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "쿠폰 / 패스",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // 우측 상단: 지도
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(8.dp)
                    .clickable(onClick = onOpenMap),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("지도(Map)", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Row(Modifier.weight(1f)) {
            // 좌측 하단: QR
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(8.dp)
                    .clickable(onClick = onOpenQR),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("QR기능", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // 우측 하단: 커뮤니티
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(8.dp)
                    .clickable(onClick = onOpenCommunity),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("커뮤니티", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        // 관리자 메뉴 (하단에 추가)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = onOpenAdmin),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "관리자 페이지",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
