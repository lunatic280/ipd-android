package com.example.ipdapplication.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ipdapplication.R

@Composable
fun ScooterBottomSheet(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 8.dp,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 상단: 배터리 정보 + 킥보드 이미지
            TopInfoSection()

            Spacer(modifier = Modifier.height(12.dp))

            // 예상 주행거리 텍스트
            Text(
                text = "약 8km 주행할 수 있어요!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 요금 칩
            RateChip(priceText = "180원/분")

            Spacer(modifier = Modifier.height(16.dp))

            // BSS 안내 카드
            BssInfoCard()

            Spacer(modifier = Modifier.height(16.dp))

            // 하단 버튼 3개
            BottomButtonRow()
        }
    }
}

@Composable
private fun TopInfoSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "28%",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 간단한 배터리 게이지 (장식용)
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(14.dp)
                        .background(
                            color = Color(0xFFE5E5E5),
                            shape = RoundedCornerShape(7.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.3f)
                            .background(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(7.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Max Plus",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "389417",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888888)
                    )
                }

                TextButton(
                    onClick = { /* TODO: 신고하기 액션 */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "신고하기",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 킥보드 이미지 (리소스는 프로젝트에 맞게 교체)

    }
}

@Composable
private fun RateChip(priceText: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color(0xFFF5F5F5)
    ) {
        Text(
            text = priceText,
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BssInfoCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE6F8EB) // 연한 초록 배경
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "150m만 가면 BSS가 있어요!",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF009944)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "BSS 강남 지쿠 배터리 교체 가능\n" +
                            "홍콩반점0410 강남역점 2000원 할인 쿠폰 지급!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF444444)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 오른쪽 배너 이미지 (리소스 교체)

        }
    }
}

@Composable
private fun BottomButtonRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 지도 닫기 버튼
        OutlinedButton(
            onClick = { /* TODO: 지도 닫기 */ },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "지도닫기")
        }

        // 사운드 버튼
        OutlinedButton(
            onClick = { /* TODO: 소리 ON/OFF */ },
            modifier = Modifier.weight(0.6f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "소리 끄기"
            )
        }

        // 대여하기 버튼
        Button(
            onClick = { /* TODO: 대여하기 */ },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C853),
                contentColor = Color.White
            )
        ) {
            Text(text = "대여하기", fontWeight = FontWeight.SemiBold)
        }
    }
}
