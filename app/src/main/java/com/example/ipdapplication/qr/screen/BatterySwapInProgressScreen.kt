package com.example.ipdapplication.qr.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun BatterySwapInProgressScreen(
    slotNumber: String = "DEMO_BSS : DEMO_SLOT",
    onSwapComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 진행 단계 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "1 / 2",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF00C46E)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            AnimatedBatteryIcon()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "배터리를 넣으세요",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "스테이션에서 배터리를 가져와 주세요.\n배터리 삽입이 완료되면 완료 버튼을 눌러주세요.",
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

            Spacer(modifier = Modifier.height(60.dp))
        }

        Button(
            onClick = onSwapComplete,
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
                text = "완료",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AnimatedBatteryIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "battery")
    val chargeLevel by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "chargeLevel"
    )

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
            val strokeWidth = 3.dp.toPx()

            val capWidth = batteryWidth * 0.4f
            val capHeight = size.height * 0.08f
            val capLeft = (size.width - capWidth) / 2
            drawRoundRect(
                color = Color(0xFF00C46E),
                topLeft = Offset(capLeft, 0f),
                size = Size(capWidth, capHeight + cornerRadius),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius / 2)
            )

            drawRoundRect(
                color = Color(0xFF00C46E),
                topLeft = Offset(batteryLeft, batteryTop),
                size = Size(batteryWidth, batteryHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                style = Stroke(width = strokeWidth)
            )

            val padding = 6.dp.toPx()
            val fillHeight = (batteryHeight - padding * 2) * chargeLevel
            val fillTop = batteryTop + padding + (batteryHeight - padding * 2) * (1 - chargeLevel)

            drawRoundRect(
                color = Color(0xFF00C46E),
                topLeft = Offset(batteryLeft + padding, fillTop),
                size = Size(batteryWidth - padding * 2, fillHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius / 2)
            )
        }

        // 아래로 향하는 화살표 (배터리에 넣기)
        Canvas(modifier = Modifier.size(35.dp)) {
            val arrowPath = androidx.compose.ui.graphics.Path().apply {
                // 화살표 수직선
                moveTo(size.width * 0.5f, size.height * 0.2f)
                lineTo(size.width * 0.5f, size.height * 0.75f)

                // 화살표 머리 (아래 방향)
                moveTo(size.width * 0.3f, size.height * 0.6f)
                lineTo(size.width * 0.5f, size.height * 0.8f)
                lineTo(size.width * 0.7f, size.height * 0.6f)
            }
            drawPath(
                path = arrowPath,
                color = Color.White,
                style = Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BatterySwapInProgressScreenPreview() {
    BatterySwapInProgressScreen(
        onSwapComplete = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AnimatedBatteryIconPreview() {
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        AnimatedBatteryIcon()
    }
}
