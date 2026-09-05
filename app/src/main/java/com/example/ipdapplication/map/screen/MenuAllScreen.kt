package com.example.ipdapplication.map.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.map.menu.viewmodel.MenuViewModel
import com.example.ipdapplication.map.screen.place.MenuItem
import com.example.ipdapplication.map.screen.place.PlaceDarkText
import com.example.ipdapplication.map.screen.place.PlaceGrayBorder
import com.example.ipdapplication.map.screen.place.PlaceGrayText
import com.example.ipdapplication.map.screen.place.PlaceMenuListItem
import com.example.ipdapplication.map.screen.place.PlaceMintGreen

@Composable
fun MenuAllScreen(
    placeId: Long,
    placeName: String,
    onBackClick: () -> Unit,
    menuViewModel: MenuViewModel = hiltViewModel()
) {
    val menuState by menuViewModel.state.collectAsState()

    LaunchedEffect(placeId) {
        menuViewModel.loadMenusByPlaceId(placeId)
    }

    val menuItems = menuState.menus.map { menu ->
        MenuItem(
            name = menu.name,
            description = menu.description,
            price = menu.price,
            imageUrl = menu.imageUrl
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 헤더
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = PlaceDarkText
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = placeName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PlaceDarkText
                    )
                    Text(
                        text = "메뉴",
                        fontSize = 14.sp,
                        color = PlaceGrayText
                    )
                }
            }

            Divider(color = PlaceGrayBorder, thickness = 1.dp)

            when {
                menuState.loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PlaceMintGreen)
                    }
                }
                menuItems.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "등록된 메뉴가 없어요",
                            fontSize = 16.sp,
                            color = PlaceGrayText
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        itemsIndexed(menuItems) { index, item ->
                            PlaceMenuListItem(menuItem = item)
                            if (index < menuItems.lastIndex) {
                                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
