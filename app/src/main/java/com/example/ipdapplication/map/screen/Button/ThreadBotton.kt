package com.example.ipdapplication.map.screen.Button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ipdapplication.map.screen.InfoDivider

enum class DetailTab {
    HOME, MENU, REVIEW
}

@Composable
fun ThreeButton(
    modifier: Modifier = Modifier,
    selectedTab: DetailTab,
    onTabSelected: (DetailTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)

    ) {
        TabButton(
            text = "홈",
            isSelected = selectedTab == DetailTab.HOME,
            onClick = { onTabSelected(DetailTab.HOME) },
            modifier = Modifier.weight(1f)
        )

        // === 메뉴 탭 (가운데) ===
        TabButton(
            text = "메뉴",
            isSelected = selectedTab == DetailTab.MENU,
            onClick = { onTabSelected(DetailTab.MENU) },
            modifier = Modifier.weight(1f)
        )

        // === 리뷰 탭 ===
        TabButton(
            text = "리뷰",
            isSelected = selectedTab == DetailTab.REVIEW,
            onClick = { onTabSelected(DetailTab.REVIEW) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 글자 색은 항상 동일 (예: 검정)
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            // 아래 인디케이터 바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        if (isSelected) Color(0xFF444444) // 진한 회색
                        else Color.Transparent
                    )
            )
        }
    }
}

@Composable
fun PlaceDetailWithTabsPreviewContainer() {
    var selectedTab by remember { mutableStateOf(DetailTab.HOME) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ThreeButton(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 탭에 따른 아래 내용
        when (selectedTab) {
            DetailTab.HOME -> Text(
                text = "홈 컨텐츠 화면",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            DetailTab.MENU -> Text(
                text = "메뉴 컨텐츠 화면",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            DetailTab.REVIEW -> Text(
                text = "리뷰 컨텐츠 화면",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceDetailTabsPreview() {
    MaterialTheme {
        PlaceDetailWithTabsPreviewContainer()
    }
}
