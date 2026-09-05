package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlaceHeader(
    placeName: String,
    batteryCount: Int,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = placeName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PlaceDarkText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = PlaceMintGreen,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("${batteryCount}개")
                    }
                    append("의 배터리팩을 교체 중이에요")
                },
                fontSize = 14.sp,
                color = PlaceDarkText
            )
        }

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (isFavorite) "즐겨찾기 해제" else "즐겨찾기",
                tint = if (isFavorite) PlaceYellow else Color(0xFFBDBDBD),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceHeaderPreview() {
    PlaceHeader(
        placeName = "샘플 맛집",
        batteryCount = 2,
        isFavorite = true,
        onFavoriteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceHeaderNotFavoritePreview() {
    PlaceHeader(
        placeName = "샘플 맛집",
        batteryCount = 5,
        isFavorite = false,
        onFavoriteClick = {}
    )
}
