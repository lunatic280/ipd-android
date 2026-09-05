package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ipdapplication.map.dto.PlaceDto

@Composable
fun PlaceInfoTexts(
    place: PlaceDto,
    location: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = place.description ?: "설명이 없습니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E)
        )
        Text(
            text = "등록일: ${place.createdAt ?: "미상"}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E)
        )
        Text(
            text = location,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8E8E8E)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceInfoTextsPreview() {
    PlaceInfoTexts(
        place = PlaceDto(
            id = 1,
            title = "샘플",
            description = "미리보기 설명",
            latitude = 0.0,
            longitude = 0.0,
            createdAt = "2025-12-04"
        ),
        location = "전시용 가상 주소"
    )
}
