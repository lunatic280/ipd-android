package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.util.Locale

data class MenuItem(
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String? = null
)

@Composable
fun PlaceMenuContent(
    menuItems: List<MenuItem>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "메뉴",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PlaceDarkText
            )

            if (menuItems.isNotEmpty()) {
                Row(
                    modifier = Modifier.clickable(onClick = onViewAllClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "전체보기",
                        fontSize = 14.sp,
                        color = PlaceGrayText
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "전체보기",
                        tint = PlaceGrayText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (menuItems.isEmpty()) {
            Text(
                text = "등록된 메뉴가 없어요",
                fontSize = 14.sp,
                color = PlaceGrayText
            )
        } else {
            menuItems.forEachIndexed { index, item ->
                PlaceMenuListItem(menuItem = item)
                if (index < menuItems.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun PlaceMenuListItem(
    menuItem: MenuItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            if (!menuItem.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(menuItem.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = menuItem.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "No\nImage",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = menuItem.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = PlaceDarkText
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (menuItem.description.isNotBlank()) {
                Text(
                    text = menuItem.description,
                    fontSize = 13.sp,
                    color = PlaceGrayText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = String.format(Locale.KOREA, "%,d원", menuItem.price),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF00C46E)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceMenuContentPreview() {
    PlaceMenuContent(
        menuItems = listOf(
            MenuItem(
                name = "매콤 맛볼래 세트",
                description = "부채살 스테이크와 구운 채소가 담긴 든든한 세트",
                price = 18900
            ),
            MenuItem(
                name = "로제 파스타",
                description = "크림과 토마토의 조합으로 부드럽게 즐기는 로제 파스타",
                price = 19900
            ),
            MenuItem(
                name = "마라 로제 떡볶이",
                description = "마라 소스와 로제가 어우러진 매콤한 떡볶이",
                price = 21900
            ),
            MenuItem(
                name = "감자튀김",
                description = "",
                price = 5900
            )
        ),
        onViewAllClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceMenuListItemPreview() {
    PlaceMenuListItem(
        menuItem = MenuItem(
            name = "매콤 맛볼래 세트",
            description = "부채살 스테이크와 구운 채소가 담긴 든든한 세트",
            price = 18900
        )
    )
}
