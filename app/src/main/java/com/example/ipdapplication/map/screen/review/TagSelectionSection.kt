package com.example.ipdapplication.map.screen.review

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ipdapplication.map.review.dto.TagDto
import com.example.ipdapplication.map.review.dto.TagType

/**
 * 태그 선택 섹션
 */
@Composable
fun TagSelectionSection(
    label: String,
    tags: List<String>,
    selectedTags: Set<String>,
    onTagToggle: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = LabelGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tags.forEach { tag ->
                TagChip(
                    text = tag,
                    isSelected = selectedTags.contains(tag),
                    onClick = { onTagToggle(tag) }
                )
            }
            // 화살표 아이콘
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "더보기",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TagSelectionSectionPreview() {
    TagSelectionSection(
        label = "지쿠 플레이스는",
        tags = listOf("맛있어요", "양이 많아요", "간이 적당해요", "사이드가 맛있어요"),
        selectedTags = setOf("맛있어요", "간이 적당해요"),
        onTagToggle = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun TagSelectionSectionBssPreview() {
    TagSelectionSection(
        label = "지쿠 BSS는",
        tags = listOf("배터리가 많아요", "찾기 쉬워요", "재밌어요", "교체가 편리해요"),
        selectedTags = setOf("찾기 쉬워요", "재밌어요"),
        onTagToggle = {}
    )
}

/**
 * 태그 선택 섹션 (TagDto 사용)
 */
@Composable
fun TagSelectionSectionWithIds(
    label: String,
    tags: List<TagDto>,
    selectedTagIds: Set<Long>,
    onTagToggle: (Long) -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = LabelGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tags.forEach { tag ->
                TagChip(
                    text = tag.name,
                    isSelected = selectedTagIds.contains(tag.id),
                    onClick = { onTagToggle(tag.id) }
                )
            }
            // 화살표 아이콘
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "더보기",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TagSelectionSectionWithIdsPreview() {
    TagSelectionSectionWithIds(
        label = "지쿠 플레이스는",
        tags = listOf(
            TagDto(1L, "맛있어요", TagType.RESTAURANT),
            TagDto(2L, "양이 많아요", TagType.RESTAURANT),
            TagDto(3L, "간이 적당해요", TagType.COMMON),
            TagDto(4L, "사이드가 맛있어요", TagType.RESTAURANT)
        ),
        selectedTagIds = setOf(1L, 3L),
        onTagToggle = {}
    )
}
