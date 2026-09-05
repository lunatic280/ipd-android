package com.example.ipdapplication.qr.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ipdapplication.cupon.dto.CouponData
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun BatterySwapCompleteScreen(
    slotNumber: String = "DEMO_BSS : DEMO_SLOT",
    autoNavigateDelayMs: Long = 3000,
    issuedCoupon: CouponData? = null,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 쿠폰 애니메이션 표시 여부 (쿠폰이 발급된 경우에만 표시)
    var showCouponAnimation by remember { mutableStateOf(issuedCoupon != null) }

    LaunchedEffect(Unit) {
        if (autoNavigateDelayMs > 0) {
            delay(autoNavigateDelayMs)
            onConfirmClick()
        }
    }

    // 쿠폰 애니메이션 자동 종료
    LaunchedEffect(showCouponAnimation) {
        if (showCouponAnimation) {
            delay(4500)
            showCouponAnimation = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 메인 콘텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                BatteryCompleteIcon()

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "배터리 교체 완료!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "배터리를 정상적으로 교체했어요.\n이제 안전하게 출발해 주세요!",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 슬롯 번호 강조 박스
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = slotNumber,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 쿠폰 지급 안내 (발급된 쿠폰 정보 표시)
                if (issuedCoupon != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉 쿠폰이 지급되었습니다!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00C46E)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 발급된 쿠폰 카드
                        IssuedCouponCard(coupon = issuedCoupon)
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))
            }

            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C46E)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "확인",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // 쿠폰 떠다니는 애니메이션 오버레이
        if (showCouponAnimation) {
            CouponCelebrationOverlay(
                couponName = issuedCoupon?.name ?: "쿠폰",
                couponPrice = issuedCoupon?.price ?: "",
                onDismiss = { showCouponAnimation = false }
            )
        }
    }
}

/**
 * 발급된 쿠폰 카드
 */
@Composable
fun IssuedCouponCard(
    coupon: CouponData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFF8E1),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 쿠폰 아이콘
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(0xFFFFD700),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎫", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 쿠폰 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coupon.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = coupon.price,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B00)
                )
                if (coupon.description.isNotEmpty()) {
                    Text(
                        text = coupon.description,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }

            // 소스 표시
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF00C46E),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = coupon.source,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * 쿠폰 축하 오버레이 - 여러 쿠폰이 아래에서 위로 떠오르는 애니메이션
 */
@Composable
fun CouponCelebrationOverlay(
    couponName: String = "쿠폰",
    couponPrice: String = "",
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val couponCount = 12

    // 각 쿠폰의 시작 위치와 속성을 기억
    val couponStates = remember {
        List(couponCount) { index ->
            CouponState(
                startX = Random.nextFloat(),
                delay = index * 200L,
                duration = 2500 + Random.nextInt(1000),
                size = 50 + Random.nextInt(20),
                rotationSpeed = if (Random.nextBoolean()) 1f else -1f
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
    ) {
        // 중앙 축하 메시지
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎊",
                fontSize = 60.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "축하합니다!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "쿠폰이 지급되었어요",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
            // 발급된 쿠폰 정보 표시
            if (couponPrice.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFFFD700),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = couponName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8B4513)
                        )
                        Text(
                            text = couponPrice,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B00)
                        )
                    }
                }
            }
        }

        // 떠다니는 쿠폰들
        couponStates.forEach { state ->
            FloatingCouponItem(couponState = state)
        }
    }
}

/**
 * 쿠폰 상태 데이터 클래스
 */
data class CouponState(
    val startX: Float,      // 0~1 사이의 시작 X 위치 비율
    val delay: Long,        // 시작 지연 시간
    val duration: Int,      // 애니메이션 지속 시간
    val size: Int,          // 쿠폰 크기
    val rotationSpeed: Float // 회전 방향 (1 또는 -1)
)

/**
 * 개별 떠다니는 쿠폰 아이템
 */
@Composable
fun FloatingCouponItem(
    couponState: CouponState,
    modifier: Modifier = Modifier
) {
    var isAnimating by remember { mutableStateOf(false) }

    // 지연 후 애니메이션 시작
    LaunchedEffect(Unit) {
        delay(couponState.delay)
        isAnimating = true
    }

    if (isAnimating) {
        // Y 위치 애니메이션 (아래에서 위로)
        val yOffset by animateFloatAsState(
            targetValue = -1.2f,
            animationSpec = tween(
                durationMillis = couponState.duration,
                easing = LinearOutSlowInEasing
            ),
            label = "yOffset"
        )

        // 투명도 애니메이션
        val alpha by animateFloatAsState(
            targetValue = if (yOffset < -0.7f) 0f else 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ),
            label = "alpha"
        )

        // 좌우 흔들림
        val infiniteTransition = rememberInfiniteTransition(label = "wobble")
        val wobbleX by infiniteTransition.animateFloat(
            initialValue = -20f,
            targetValue = 20f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "wobbleX"
        )

        // 회전 애니메이션
        val rotation by infiniteTransition.animateFloat(
            initialValue = -15f * couponState.rotationSpeed,
            targetValue = 15f * couponState.rotationSpeed,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "rotation"
        )

        // 크기 펄스 애니메이션
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight
            val xPosition = screenWidth * couponState.startX
            val yPosition = screenHeight * (1f + yOffset)

            Box(
                modifier = modifier
                    .offset(
                        x = xPosition - (couponState.size / 2).dp + wobbleX.dp,
                        y = yPosition
                    )
                    .alpha(alpha)
                    .rotate(rotation)
                    .scale(scale)
            ) {
                CouponIcon(size = couponState.size)
            }
        }
    }
}

/**
 * 쿠폰 아이콘 컴포저블
 */
@Composable
fun CouponIcon(
    size: Int = 60,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(
                color = Color(0xFFFFD700),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎫",
                fontSize = (size * 0.4f).sp
            )
            Text(
                text = "쿠폰",
                fontSize = (size * 0.18f).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        }
    }
}

@Composable
private fun BatteryCompleteIcon() {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(80.dp, 100.dp)) {
            val batteryWidth = size.width * 0.8f
            val batteryHeight = size.height * 0.85f
            val batteryLeft = (size.width - batteryWidth) / 2
            val batteryTop = size.height * 0.1f
            val cornerRadius = 8.dp.toPx()

            val capWidth = batteryWidth * 0.4f
            val capHeight = size.height * 0.08f
            val capLeft = (size.width - capWidth) / 2
            drawRoundRect(
                color = Color(0xFF00C46E),
                topLeft = Offset(capLeft, 0f),
                size = Size(capWidth, capHeight + cornerRadius),
                cornerRadius = CornerRadius(cornerRadius / 2)
            )

            drawRoundRect(
                color = Color(0xFF00C46E),
                topLeft = Offset(batteryLeft, batteryTop),
                size = Size(batteryWidth, batteryHeight),
                cornerRadius = CornerRadius(cornerRadius)
            )

            val checkPath = Path().apply {
                val centerX = size.width / 2
                val centerY = batteryTop + batteryHeight / 2

                moveTo(centerX - 15.dp.toPx(), centerY)
                lineTo(centerX - 3.dp.toPx(), centerY + 12.dp.toPx())
                lineTo(centerX + 15.dp.toPx(), centerY - 10.dp.toPx())
            }
            drawPath(
                path = checkPath,
                color = Color.White,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

// ==================== Previews ====================

// 테스트용 쿠폰 데이터
private val previewCoupon = CouponData(
    name = "BSS 교체 감사 쿠폰",
    price = "2,000원",
    description = "배터리 교체 감사 쿠폰 (5분 내 사용)",
    source = "BSS"
)

@Preview(showBackground = true, name = "완료 화면 (쿠폰 있음)")
@Composable
private fun BatterySwapCompleteScreenWithCouponPreview() {
    BatterySwapCompleteScreen(
        autoNavigateDelayMs = 0,
        issuedCoupon = previewCoupon,
        onConfirmClick = {}
    )
}

@Preview(showBackground = true, name = "완료 화면 (쿠폰 없음)")
@Composable
private fun BatterySwapCompleteScreenNoCouponPreview() {
    BatterySwapCompleteScreen(
        autoNavigateDelayMs = 0,
        issuedCoupon = null,
        onConfirmClick = {}
    )
}

@Preview(showBackground = true, name = "쿠폰 오버레이")
@Composable
private fun CouponCelebrationOverlayPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CouponCelebrationOverlay(
            couponName = "BSS 교체 감사 쿠폰",
            couponPrice = "2,000원",
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, name = "발급된 쿠폰 카드")
@Composable
private fun IssuedCouponCardPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        IssuedCouponCard(coupon = previewCoupon)
    }
}

@Preview(showBackground = true, name = "쿠폰 아이콘")
@Composable
private fun CouponIconPreview() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        CouponIcon(size = 60)
    }
}

@Preview(showBackground = true, name = "배터리 아이콘")
@Composable
private fun BatteryCompleteIconPreview() {
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        BatteryCompleteIcon()
    }
}
