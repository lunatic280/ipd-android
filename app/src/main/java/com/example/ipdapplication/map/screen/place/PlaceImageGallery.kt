package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PlaceImageGallery(
    mainImageUrl: String?,
    subImageUrls: List<String>,
    totalImageCount: Int,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extraCount = (totalImageCount - 3).coerceAtLeast(0)
    val sub1 = subImageUrls.getOrNull(0)
    val sub2 = subImageUrls.getOrNull(1)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // 메인 이미지
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE8F5E9))
            ) {
                if (mainImageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(mainImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "대표 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFB0BEC5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "이미지 없음",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                }
            }

            // 서브 이미지
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ThumbImageBox(
                    uri = sub1,
                    background = Color(0xFFEAF7FF),
                    showClose = true,
                    onCloseClick = onCloseClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                ThumbImageBox(
                    uri = sub2,
                    background = Color(0xFFE3F2FD),
                    showClose = false,
                    onCloseClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD7CCC8))
                        .clickable { /* TODO: 추가 이미지 클릭 처리 */ },
                    contentAlignment = Alignment.Center
                ) {
                    if (extraCount > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+$extraCount",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            text = "추가 이미지",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThumbImageBox(
    uri: String?,
    background: Color,
    showClose: Boolean,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
    ) {
        if (uri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .crossfade(true)
                    .build(),
                contentDescription = "추가 이미지",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (showClose) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceImageGalleryPreview() {
    PlaceImageGallery(
        mainImageUrl = "https://example.invalid/images/400/200",
        subImageUrls = listOf(
            "https://example.invalid/images/200/200?2",
            "https://example.invalid/images/200/200?3"
        ),
        totalImageCount = 5,
        onBackClick = {},
        onCloseClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceImageGalleryFewImagesPreview() {
    PlaceImageGallery(
        mainImageUrl = null,
        subImageUrls = listOf(),
        totalImageCount = 0,
        onBackClick = {},
        onCloseClick = {}
    )
}
