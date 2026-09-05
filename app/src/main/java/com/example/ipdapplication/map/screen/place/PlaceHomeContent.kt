package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Place HOME 정보 영역
 */
@Composable
fun PlaceHomeContent(
    address: String,
    businessStatus: String?,
    lastOrderTime: String?,
    phoneNumber: String?,
    likeCount: Int? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "\uc8fc\uc18c: $address",
            fontSize = 15.sp,
            color = PlaceDarkText
        )

        Spacer(modifier = Modifier.height(8.dp))

        businessStatus?.let { status ->
            val statusLine = buildString {
                append("\uc0c1\ud0dc: ")
                append(status)
                if (!lastOrderTime.isNullOrBlank()) {
                    append(" / $lastOrderTime")
                }
            }
            Text(
                text = statusLine,
                fontSize = 15.sp,
                color = PlaceDarkText
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        likeCount?.let {
            Text(
                text = "\uc88b\uc544\uc694: $it",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = PlaceDarkText
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        phoneNumber?.let {
            Text(
                text = "\uc804\ud654\ubc88\ud638: $it",
                fontSize = 15.sp,
                color = PlaceDarkText
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceHomeContentPreview() {
    PlaceHomeContent(
        address = "전시용 가상 주소",
        businessStatus = "\uc601\uc5c5 \uc911",
        lastOrderTime = "21:00",
        phoneNumber = "000-0000-0000",
        likeCount = 12
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceHomeContentNoLastOrderPreview() {
    PlaceHomeContent(
        address = "전시용 가상 주소",
        businessStatus = "\uc601\uc5c5 \uc624\ud508 \uc608\uc815",
        lastOrderTime = null,
        phoneNumber = "000-0000-0000",
        likeCount = null
    )
}
