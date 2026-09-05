package com.example.ipdapplication.admin.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ipdapplication.map.menu.dto.MenuDto
import com.example.ipdapplication.map.menu.viewmodel.MenuViewModel
import com.example.ipdapplication.map.viewModel.PlaceViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMenuScreen(
    menuViewModel: MenuViewModel = hiltViewModel(),
    placeViewModel: PlaceViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val menuState by menuViewModel.state.collectAsState()
    val placeState by placeViewModel.state.collectAsState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf<MenuDto?>(null) }

    var menuName by remember { mutableStateOf("") }
    var menuPrice by remember { mutableStateOf("") }
    var menuDescription by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var selectedPlaceId by remember { mutableStateOf<Long?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(Unit) {
        placeViewModel.loadAll()
        menuViewModel.loadAllMenus()
    }

    LaunchedEffect(placeState.places) {
        if (selectedPlaceId == null && placeState.places.isNotEmpty()) {
            selectedPlaceId = placeState.places.first().id
        }
    }

    // 선택된 Place에 따라 메뉴 필터링
    val filteredMenus = if (selectedPlaceId != null) {
        menuState.menus.filter { it.placeId == selectedPlaceId }
    } else {
        menuState.menus
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "관리자 메뉴 관리",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C46E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (selectedPlaceId != null) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color(0xFF00C46E),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "메뉴 추가")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlaceSelector(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    selectedPlaceId = selectedPlaceId,
                    places = placeState.places,
                    onPlaceSelected = {
                        selectedPlaceId = it
                        expanded = false
                    }
                )

                when {
                    menuState.loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF00C46E))
                        }
                    }

                    selectedPlaceId == null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "등록할 Place가 없습니다. 먼저 Place를 등록하세요.",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }

                    filteredMenus.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "등록된 메뉴가 없습니다. + 버튼으로 추가하세요.",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredMenus) { menu ->
                                MenuListItem(
                                    menu = menu,
                                    onClick = {
                                        selectedMenu = menu
                                        showDetailDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            menuState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(error)
                }
            }
        }

        // 메뉴 추가 다이얼로그
        if (showAddDialog) {
            MenuAddEditDialog(
                title = "메뉴 추가",
                menuName = menuName,
                menuPrice = menuPrice,
                menuDescription = menuDescription,
                selectedImageUri = selectedImageUri,
                onMenuNameChange = { menuName = it },
                onMenuPriceChange = { menuPrice = it.filter { c -> c.isDigit() } },
                onMenuDescriptionChange = { menuDescription = it },
                onImageClick = { imagePickerLauncher.launch("image/*") },
                onConfirm = {
                    val placeId = selectedPlaceId
                    if (placeId != null && menuName.isNotBlank() && menuPrice.isNotBlank() && selectedImageUri != null) {
                        val file = uriToFile(context, selectedImageUri!!)
                        if (file != null) {
                            menuViewModel.createMenu(
                                placeId = placeId,
                                name = menuName,
                                price = menuPrice.toIntOrNull() ?: 0,
                                description = menuDescription,
                                imageFile = file
                            )
                            showAddDialog = false
                            menuName = ""
                            menuPrice = ""
                            menuDescription = ""
                            selectedImageUri = null
                        }
                    }
                },
                onDismiss = {
                    showAddDialog = false
                    menuName = ""
                    menuPrice = ""
                    menuDescription = ""
                    selectedImageUri = null
                },
                confirmEnabled = selectedPlaceId != null && menuName.isNotBlank() && menuPrice.isNotBlank() && selectedImageUri != null,
                isEdit = false,
                existingImageUrl = null
            )
        }

        // 메뉴 상세 다이얼로그
        if (showDetailDialog && selectedMenu != null) {
            MenuDetailDialog(
                menu = selectedMenu!!,
                onDismiss = {
                    showDetailDialog = false
                    selectedMenu = null
                },
                onEditClick = {
                    showDetailDialog = false
                    menuName = selectedMenu!!.name
                    menuPrice = selectedMenu!!.price.toString()
                    menuDescription = selectedMenu!!.description
                    selectedImageUri = null
                    showEditDialog = true
                },
                onDeleteClick = {
                    showDetailDialog = false
                    showDeleteConfirmDialog = true
                }
            )
        }

        // 메뉴 수정 다이얼로그
        if (showEditDialog && selectedMenu != null) {
            MenuAddEditDialog(
                title = "메뉴 수정",
                menuName = menuName,
                menuPrice = menuPrice,
                menuDescription = menuDescription,
                selectedImageUri = selectedImageUri,
                onMenuNameChange = { menuName = it },
                onMenuPriceChange = { menuPrice = it.filter { c -> c.isDigit() } },
                onMenuDescriptionChange = { menuDescription = it },
                onImageClick = { imagePickerLauncher.launch("image/*") },
                onConfirm = {
                    val menu = selectedMenu!!
                    val placeId = menu.placeId ?: selectedPlaceId
                    if (placeId != null && menuName.isNotBlank() && menuPrice.isNotBlank()) {
                        val file = selectedImageUri?.let { uriToFile(context, it) }
                        menuViewModel.updateMenu(
                            menuId = menu.id!!,
                            placeId = placeId,
                            name = menuName,
                            price = menuPrice.toIntOrNull() ?: 0,
                            description = menuDescription,
                            imageFile = file
                        )
                        showEditDialog = false
                        selectedMenu = null
                        menuName = ""
                        menuPrice = ""
                        menuDescription = ""
                        selectedImageUri = null
                    }
                },
                onDismiss = {
                    showEditDialog = false
                    selectedMenu = null
                    menuName = ""
                    menuPrice = ""
                    menuDescription = ""
                    selectedImageUri = null
                },
                confirmEnabled = menuName.isNotBlank() && menuPrice.isNotBlank(),
                isEdit = true,
                existingImageUrl = selectedMenu?.imageUrl
            )
        }

        // 삭제 확인 다이얼로그
        if (showDeleteConfirmDialog && selectedMenu != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteConfirmDialog = false
                    selectedMenu = null
                },
                title = {
                    Text(
                        text = "메뉴 삭제",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text("'${selectedMenu!!.name}' 메뉴를 삭제하시겠습니까?\n이 작업은 취소할 수 없습니다.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            menuViewModel.deleteMenu(selectedMenu!!.id!!)
                            showDeleteConfirmDialog = false
                            selectedMenu = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        )
                    ) {
                        Text("삭제")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmDialog = false
                            selectedMenu = null
                        }
                    ) {
                        Text("취소", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
private fun MenuAddEditDialog(
    title: String,
    menuName: String,
    menuPrice: String,
    menuDescription: String,
    selectedImageUri: Uri?,
    onMenuNameChange: (String) -> Unit,
    onMenuPriceChange: (String) -> Unit,
    onMenuDescriptionChange: (String) -> Unit,
    onImageClick: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmEnabled: Boolean,
    isEdit: Boolean,
    existingImageUrl: String?
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = menuName,
                    onValueChange = onMenuNameChange,
                    label = { Text("메뉴 이름") },
                    placeholder = { Text("예) 매콤 떡볶이") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = menuPrice,
                    onValueChange = onMenuPriceChange,
                    label = { Text("가격(원)") },
                    placeholder = { Text("예) 18900") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = menuDescription,
                    onValueChange = onMenuDescriptionChange,
                    label = { Text("설명") },
                    placeholder = { Text("메뉴 설명을 입력하세요") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Column {
                    Text(
                        text = if (isEdit) "이미지 (변경시에만 선택)" else "이미지",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onImageClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            selectedImageUri != null -> {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "선택한 이미지",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            isEdit && !existingImageUrl.isNullOrBlank() -> {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(existingImageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "기존 이미지",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            else -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "이미지 추가",
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "이미지 선택",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = confirmEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C46E)
                )
            ) {
                Text(if (isEdit) "수정" else "등록")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = Color.Gray)
            }
        }
    )
}

@Composable
private fun MenuDetailDialog(
    menu: MenuDto,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = menu.name,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 이미지
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    if (menu.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(menu.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = menu.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("이미지 없음", color = Color.Gray)
                    }
                }

                // 가격
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "가격",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = String.format("%,d원", menu.price),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF00C46E)
                    )
                }

                // 설명
                if (menu.description.isNotBlank()) {
                    Column {
                        Text(
                            text = "설명",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = menu.description,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "수정",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("수정")
                    }
                    Button(
                        onClick = onDeleteClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "삭제",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("삭제")
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기", color = Color.Gray)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceSelector(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedPlaceId: Long?,
    places: List<com.example.ipdapplication.map.dto.PlaceDto>,
    onPlaceSelected: (Long) -> Unit
) {
    val selectedPlaceName = places.find { it.id == selectedPlaceId }?.title ?: "플레이스 선택"
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = selectedPlaceName,
            onValueChange = {},
            readOnly = true,
            label = { Text("플레이스") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            places.forEach { place ->
                val id = place.id
                if (id != null) {
                    DropdownMenuItem(
                        text = { Text(place.title) },
                        onClick = { onPlaceSelected(id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuListItem(
    menu: MenuDto,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                if (menu.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(menu.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = menu.name,
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

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = menu.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )

                if (menu.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = menu.description,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = String.format("%,d원", menu.price),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF00C46E)
                )
            }
        }
    }
}

private fun uriToFile(context: android.content.Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        file
    } catch (e: Exception) {
        null
    }
}

// Preview용 샘플 데이터
private val sampleMenus = listOf(
    MenuDto(
        id = 1,
        placeId = 1,
        name = "매콤 떡볶이",
        price = 12000,
        description = "매콤하고 쫄깃한 떡볶이",
        imageUrl = ""
    ),
    MenuDto(
        id = 2,
        placeId = 1,
        name = "순대",
        price = 8000,
        description = "찹쌀순대와 야채순대 모둠",
        imageUrl = ""
    ),
    MenuDto(
        id = 3,
        placeId = 1,
        name = "튀김 모둠",
        price = 10000,
        description = "고구마, 김말이, 새우튀김",
        imageUrl = ""
    )
)

@Preview(showBackground = true)
@Composable
private fun MenuListItemPreview() {
    MenuListItem(
        menu = MenuDto(
            id = 1,
            placeId = 1,
            name = "매콤 떡볶이",
            price = 18900,
            description = "매콤하고 쫄깃한 국물 떡볶이입니다",
            imageUrl = ""
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuListPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sampleMenus.forEach { menu ->
            MenuListItem(menu = menu)
        }
    }
}
