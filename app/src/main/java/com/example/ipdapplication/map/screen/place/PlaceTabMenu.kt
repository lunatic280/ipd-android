package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class PlaceTab(val title: String) {
    HOME("홈"),
    MENU("메뉴"),
    REVIEW("리뷰")
}

@Composable
fun PlaceTabMenu(
    selectedTab: PlaceTab,
    onTabSelected: (PlaceTab) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        PlaceTab.entries.forEach { tab ->
            PlaceTabItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PlaceTabItem(
    tab: PlaceTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = tab.title,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) PlaceDarkText else PlaceGrayText,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    if (isSelected) PlaceDarkText else Color.Transparent
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceTabMenuHomePreview() {
    PlaceTabMenu(
        selectedTab = PlaceTab.HOME,
        onTabSelected = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceTabMenuMenuPreview() {
    PlaceTabMenu(
        selectedTab = PlaceTab.MENU,
        onTabSelected = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceTabMenuReviewPreview() {
    PlaceTabMenu(
        selectedTab = PlaceTab.REVIEW,
        onTabSelected = {}
    )
}
