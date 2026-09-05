package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ipdapplication.map.dto.PlaceDto

@Composable
fun PlaceHeaderSection(
    place: PlaceDto,
    commentCount: Int,
    likeCount: Long,
    liked: Boolean,
    onToggleLike: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = place.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF171717)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString {
                    val countStr = "$commentCount"
                    append(countStr)
                    addStyle(
                        SpanStyle(
                            color = Color(0xFF00C46E),
                            fontWeight = FontWeight.SemiBold
                        ),
                        start = 0,
                        end = countStr.length
                    )
                    append("개의 배터리 리뷰가 등록됐어요")
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
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggleLike) {
                    Icon(
                        imageVector = if (liked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "좋아요",
                        tint = if (liked) Color(0xFFE91E63) else Color(0xFF00C46E)
                    )
                }
                Text(
                    text = likeCount.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF4B4B4B)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceHeaderSectionPreview() {
    PlaceHeaderSection(
        place = PlaceDto(
            id = 1L,
            title = "샘플 식당",
            description = "대표 메뉴가 맛있는 곳",
            latitude = 0.0,
            longitude = 0.0
        ),
        commentCount = 12,
        likeCount = 34,
        liked = true,
        onToggleLike = {}
    )
}
