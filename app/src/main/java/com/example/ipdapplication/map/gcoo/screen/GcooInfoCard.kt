package com.example.ipdapplication.map.gcoo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ipdapplication.R
import com.example.ipdapplication.map.gcoo.dto.GcooDto

@Composable
fun GcooInfoCard(
    gcoo: GcooDto,
    onReserveClick: () -> Unit,
    onCloseClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    bssDistance: Int? = 150,  // BSS까지 거리 (null이면 BSS 배너 숨김)
    bssName: String = "BSS 강남 지쿠",
    couponStoreName: String = "홍콩반점0410 강남역점",
    couponAmount: Int = 2000,
    couponImageUrl: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // ===== 상단: 배터리 정보 + 신고하기 + 킥보드 이미지 =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 왼쪽: 배터리 %, 모델명, ID
                Column(modifier = Modifier.weight(1f)) {
                    // 배터리 % + 모델명 + 신고하기
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        Spacer(modifier = Modifier.width(12.dp))

                        // 모델명 + ID
                        Column {
                            Text(
                                text = gcoo.subTitle.split(" - ").firstOrNull() ?: "Max Plus",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                text = gcoo.subTitle.split(" - ").lastOrNull() ?: "",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // 신고하기 버튼
                        Row(
                            modifier = Modifier
                                .clickable { onReportClick() }
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🚨",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "신고하기",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 주행 가능 거리
                    Text(
                        text = "약 ${gcoo.distance}km 주행할 수 있어요!",
                        fontSize = 14.sp,
                        color = Color(0xFF00C46E),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 가격 태그
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = gcoo.description,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }

                // 오른쪽: 킥보드 이미지
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mobility_marker),
                        contentDescription = "모빌리티 아이콘",
                        modifier = Modifier.size(75.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== BSS 배너 (녹색 배경) =====
            if (bssDistance != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 왼쪽: BSS 정보
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${bssDistance}m만 가면 BSS가 있어요!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00C46E)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "$bssName 배터리 교체 가능",
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "$couponStoreName ${couponAmount}원 할인 쿠폰 지급!",
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // 오른쪽: 쿠폰 이미지
                        if (!couponImageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = couponImageUrl,
                                contentDescription = "쿠폰 이미지",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // 기본 이미지 (홍콩반점 스타일)
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFE0B2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "🍜", fontSize = 24.sp)
                                    Text(
                                        text = "쿠폰",
                                        fontSize = 10.sp,
                                        color = Color(0xFF8B4513)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 하단 버튼들 (한 줄에 배치) =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 지도닫기 버튼
                OutlinedButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF666666)
                    )
                ) {
                    Text(
                        text = "지도닫기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // 소리 버튼

                // 대여하기 버튼
                Button(
                    onClick = onReserveClick,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C46E),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "대여하기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GcooInfoCardPreview() {
    val sample = GcooDto(
        id = 1L,
        charge = 28,
        distance = 8,
        subTitle = "Max Plus - 389417",
        description = "180원/분",
        latitude = 0.0,
        longitude = 0.0,
        userId = 0L,
        imageUrl = null
    )
    GcooInfoCard(
        gcoo = sample,
        onReserveClick = {},
        bssDistance = 150,
        bssName = "BSS 강남 지쿠",
        couponStoreName = "홍콩반점0410 강남역점",
        couponAmount = 2000
    )
}

@Preview(showBackground = true)
@Composable
private fun GcooInfoCardNoBssPreview() {
    val sample = GcooDto(
        id = 1L,
        charge = 85,
        distance = 25,
        subTitle = "Max Pro - 123456",
        description = "180원/분",
        latitude = 0.0,
        longitude = 0.0,
        userId = 0L,
        imageUrl = null
    )
    GcooInfoCard(
        gcoo = sample,
        onReserveClick = {},
        bssDistance = null  // BSS 배너 숨김
    )
}
