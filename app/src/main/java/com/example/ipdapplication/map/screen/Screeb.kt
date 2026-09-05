package com.example.ipdapplication.map.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDetailScreen(
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isExpanded = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded
    val sheetScrollState = rememberScrollState()

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 280.dp,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetDragHandle = { BottomSheetDefaults.DragHandle() },
        sheetContainerColor = Color.White,
        sheetContent = {
            DetailSheetContent(
                isExpanded = isExpanded,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(sheetScrollState)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        )
    }
}

@Composable
private fun DetailSheetContent(
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        // 펼쳐졌을 때 상단 큰 이미지 영역
        AnimatedVisibility(visible = isExpanded) {
            Column {
                HerolmageSection()   // 이 Composable 은 네가 따로 구현했다고 가정
                Spacer(Modifier.height(20.dp))
            }
        }

        HeaderSection()
        Spacer(Modifier.height(16.dp))

        InfoTexts()
        Spacer(Modifier.height(14.dp))

        PriceChip()
        Spacer(Modifier.height(18.dp))

        PhotoPreviewRow()
        Spacer(Modifier.height(20.dp))

        ShortcutRow()
        Spacer(Modifier.height(16.dp))

        ActionButtons()

        AnimatedVisibility(visible = isExpanded) {
            Column {
                Spacer(Modifier.height(32.dp))
                InfoDivider()
                Spacer(Modifier.height(18.dp))
                InfoListSection()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapDetailScreenPreview() {
    MapDetailScreen()
}
