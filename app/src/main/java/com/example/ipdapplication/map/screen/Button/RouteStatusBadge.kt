package com.example.ipdapplication.map.screen.Button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng

@Composable
fun RouteStatusBadge(
    modifier: Modifier = Modifier,
    startPoint: LatLng?,
    goalPoint: LatLng?,
    message: String?
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = Color(0xF2FFFFFF),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "출발: " + (
                    startPoint?.let {
                        "(${String.format("%.4f", it.latitude)}, ${String.format("%.4f", it.longitude)})"
                    } ?: "현재 위치 기준"
                ),
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF4B4B4B)
            )
            Text(
                text = "도착: " + (
                    goalPoint?.let {
                        "(${String.format("%.4f", it.latitude)}, ${String.format("%.4f", it.longitude)})"
                    } ?: "지도를 눌러 설정"
                ),
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF4B4B4B)
            )
            message?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF00C46E)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RouteStatusBadgePreview() {
    RouteStatusBadge(
        startPoint = LatLng(0.0, 0.0),
        goalPoint = LatLng(0.0, 0.0),
        message = "약 1.2km / 10분"
    )
}
