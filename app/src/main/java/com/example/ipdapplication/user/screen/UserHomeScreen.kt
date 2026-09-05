package com.example.ipdapplication.user.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ipdapplication.R
import com.example.ipdapplication.cupon.viewmodel.CouponViewModel

// 앱 테마 색상
private val GcooGreen = Color(0xFF00C46E)
private val LightGreen = Color(0xFFB8F5D3)
private val BackgroundGray = Color(0xFFF5F5F5)
private val TextGray = Color(0xFF9E9E9E)
private val OrangeAccent = Color(0xFFFFAA00)

@Composable
fun UserHomeScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToQR: () -> Unit,
    onNavigateToCoupon: () -> Unit = {},
    onNavigateToStore: () -> Unit = {},
    onNavigateToFeed: () -> Unit = {},
    userName: String = "0523",
    userGrade: String = "WHITE",
    currentPoint: Int = 0,
    maxPoint: Int = 30,
    credit: Int = 20,
    couponCountOverride: Int? = null,
    couponViewModel: CouponViewModel? = null
) {
    val scrollState = rememberScrollState()
    val couponCount = couponCountOverride ?: run {
        val viewModel = couponViewModel ?: hiltViewModel<CouponViewModel>()
        val couponUiState by viewModel.uiState.collectAsStateWithLifecycle()
        couponUiState.availableCount
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // ===== 상단 헤더 =====
        TopHeader()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ===== 사용자 정보 카드 =====
            UserInfoCard(
                userName = userName,
                userGrade = userGrade,
                currentPoint = currentPoint,
                maxPoint = maxPoint,
                credit = credit,
                couponCount = couponCount,
                onCouponClick = onNavigateToCoupon
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 지도 / QR 스캔 카드 =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 지도 카드
                MapCard(
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMap
                )

                // QR 스캔 카드
                QrScanCard(
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToQR
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 프로모션 배너 (가로 스크롤) =====
            PromotionBanner(
                onClick = onNavigateToCoupon
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 스토어 / 지쿠피드 카드 =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 스토어 카드
                StoreCard(
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToStore
                )

                // 지쿠피드 카드
                FeedCard(
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToFeed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== 추가 메뉴 리스트 =====
            AdditionalMenuList()
        }
    }
}

@Composable
private fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // GCOO 로고
        Text(
            text = "GCOO",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = GcooGreen
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 알림 아이콘
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "알림",
                    tint = Color.Black
                )
            }

            // 메뉴 아이콘
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "메뉴",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
private fun UserInfoCard(
    userName: String,
    userGrade: String,
    currentPoint: Int,
    maxPoint: Int,
    credit: Int,
    couponCount: Int,
    onCouponClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 상단: 사용자 이름 + 헬멧 아이콘
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "${userName}님이",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "지구를 지켜준 시간",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // 헬멧 아이콘
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F0F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🪖",
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 등급 정보 + 포인트
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 등급 텍스트
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = GcooGreen,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append("$userGrade 등급")
                        }
                        withStyle(style = SpanStyle(color = TextGray)) {
                            append("  (다음등급까지 ${maxPoint}P)")
                        }
                    },
                    fontSize = 13.sp
                )

                // 포인트
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$currentPoint",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = GcooGreen
                    )
                    Text(
                        text = " / $maxPoint P",
                        fontSize = 14.sp,
                        color = TextGray,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 프로그레스 바
            LinearProgressIndicator(
                progress = { currentPoint.toFloat() / maxPoint.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = GcooGreen,
                trackColor = Color(0xFFE8E8E8)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 크레딧 / 쿠폰 패스
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 크레딧
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "크레딧",
                        fontSize = 14.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$credit",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GcooGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(OrangeAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "C",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // 구분선
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color(0xFFE0E0E0))
                )

                // 쿠폰/패스
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onCouponClick() },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "쿠폰/패스",
                        fontSize = 14.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$couponCount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GcooGreen
                    )
                    Text(
                        text = " 개",
                        fontSize = 14.sp,
                        color = GcooGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun MapCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "지도",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Slightly scale in to crop out the image's black stroke.
                        scaleX = 1.05f
                        scaleY = 1.05f
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun QrScanCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GcooGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(GcooGreen)
        ) {
            Image(
                painter = painterResource(id = R.drawable.qr),
                contentDescription = "QR 스캔",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Slightly scale in to crop out the image's black stroke.
                        scaleX = 1.05f
                        scaleY = 1.05f
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun PromotionBanner(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB8F54D)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "신규회원이라면 누구나!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "지쿠 할인쿠폰 증정",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "신규회원 웰컴 쿠폰",
                        fontSize = 10.sp,
                        color = TextGray
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "총",
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "10,000",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = GcooGreen
                        )
                        Text(
                            text = "원",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GcooGreen,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1.3f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "스토어",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "지쿠패스 구매하기",
                fontSize = 12.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.weight(1f))

            // 스토어 아이콘
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(40.dp)
                            .background(GcooGreen, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(GcooGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "G",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Row {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(OrangeAccent)
                                )
                                if (it < 2) Spacer(modifier = Modifier.width(2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1.3f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "지쿠피드",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "숏폼보고 크레딧 받기",
                fontSize = 12.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.weight(1f))

            // 피드 아이콘
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 크레딧 아이콘
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(OrangeAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "C",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    // 녹색 화살표 모양
                    Column {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(12.dp)
                                .background(GcooGreen, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(12.dp)
                                .background(GcooGreen, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdditionalMenuList() {
    val items = listOf(
        "대학생 할인" to Icons.Outlined.School,
        "결제수단" to Icons.Outlined.CreditCard,
        "탑승내역" to Icons.Outlined.History,
        "서비스안내" to Icons.Outlined.Info,
        "고객센터" to Icons.Outlined.HeadsetMic
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEach { (title, icon) ->
            MenuItemCard(title = title, icon = icon)
        }
    }
}

@Composable
private fun MenuItemCard(
    title: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF202124)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF202124)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserHomeScreenPreview() {
    UserHomeScreen(
        onNavigateToMap = {},
        onNavigateToQR = {},
        onNavigateToCoupon = {},
        couponCountOverride = 3
    )
}
