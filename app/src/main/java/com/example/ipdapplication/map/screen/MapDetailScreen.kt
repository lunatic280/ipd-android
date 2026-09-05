package com.example.ipdapplication.map.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailBottomSheet(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        tonalElevation = 2.dp,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            HeaderSection()
            Spacer(Modifier.height(16.dp))
            InfoTexts()
            Spacer(Modifier.height(14.dp))
            PriceChip()
            Spacer(Modifier.height(18.dp))
            PhotoPreviewRow()
            Spacer(Modifier.height(20.dp))
            ShortcutRow()
            Spacer(Modifier.height(12.dp))
            ActionButtons()
        }
    }
}

@Composable
fun HeaderSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Sample Restaurant",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF171717)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString {
                    append("3")
                    addStyle(
                        SpanStyle(
                            color = Color(0xFF00C46E),
                            fontWeight = FontWeight.Medium
                        ),
                        start = 0,
                        end = 1
                    )
                    append(" reviews registered")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B4B4B)
            )
        }
        Spacer(Modifier.width(12.dp))
        Surface(
            shape = CircleShape,
            color = Color(0xFFDFF7EB),
            tonalElevation = 0.dp
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "즐겨찾기",
                tint = Color(0xFF00C46E),
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun InfoTexts() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "카테고리 · 리뷰 1103",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E)
        )
        Text(
            text = "영업 · 21:00 라스트오더",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E)
        )
        Text(
            text = "58m · 샘플시",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E)
        )
    }
}

@Composable
fun PriceChip() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF2F4F6)
    ) {
        Text(
            text = "18,000원",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF363636),
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Simple photo placeholders.
 */
@Composable
fun PhotoPreviewRow() {
    val placeholders = listOf(
        Color(0xFFE8F5E9) to "Hall",
        Color(0xFFFFF3E0) to "Kitchen",
        Color(0xFFE0F7FA) to "Outside"
    )
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val itemCount = placeholders.size
        val spacing = 12.dp
        val totalSpacing = spacing * (itemCount - 1)
        val itemWidth = (maxWidth - totalSpacing) / itemCount

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            placeholders.forEach { (color, label) ->
                Box(
                    modifier = Modifier
                        .width(itemWidth)
                        .height(itemWidth * 0.65f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color(0xFF4B4B4B),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ShortcutRow() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ShortcutIcon(text = "전화", icon = Icons.Filled.Call)
        ShortcutIcon(text = "공유", icon = Icons.AutoMirrored.Rounded.Send)
    }
}

@Composable
fun ShortcutIcon(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.border(
                width = 1.dp,
                color = Color(0xFFDADADA),
                shape = CircleShape
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFF3F3F3F),
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6A6A6A)
        )
    }
}

@Composable
fun ActionButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        OutlinedButton(
            modifier = Modifier.weight(0.2f),
            shape = RoundedCornerShape(50),
            onClick = { }
        ) {
            Text(
                text = "포장",
                color = Color(0xFF00C46E),
                fontWeight = FontWeight.SemiBold
            )
        }
        Button(
            modifier = Modifier.weight(0.2f),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C46E),
                contentColor = Color.White
            ),
            onClick = { }
        ) {
            Text(
                text = "예약",
                fontWeight = FontWeight.SemiBold
            )
        }
        // 오른쪽 여백 30%
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun HerolmageSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE8F5E9)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hero Image Placeholder",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF4B4B4B),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun InfoDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFFE4E4E4),
        thickness = 1.dp
    )
}

@Composable
fun InfoListSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoItem(
            label = "주소",
            value = "전시용 가상 주소"
        )
        InfoItem(
            label = "영업시간",
            value = "매일 11:00 ~ 22:00"
        )
        InfoItem(
            label = "편의시설",
            value = "배달 지원 · 키즈존 · 주차 가능"
        )
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(70.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF363636),
            lineHeight = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailBottomSheetPreview() {
    DetailBottomSheet()
}
