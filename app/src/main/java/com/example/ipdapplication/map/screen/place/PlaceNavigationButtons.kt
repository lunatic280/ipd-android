package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 출발/도착 버튼 섹션
 */
@Composable
fun PlaceNavigationButtons(
    onDepartureClick: () -> Unit,
    onArrivalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 출발 버튼 (아웃라인)
        OutlinedButton(
            onClick = onDepartureClick,
            modifier = Modifier
                .width(120.dp)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            border = BorderStroke(1.5.dp, PlaceMintGreen),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PlaceMintGreen
            )
        ) {
            Text(
                text = "출발",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 도착 버튼 (채워진)
        Button(
            onClick = onArrivalClick,
            modifier = Modifier
                .width(120.dp)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PlaceMintGreen,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "도착",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceNavigationButtonsPreview() {
    PlaceNavigationButtons(
        onDepartureClick = {},
        onArrivalClick = {}
    )
}
