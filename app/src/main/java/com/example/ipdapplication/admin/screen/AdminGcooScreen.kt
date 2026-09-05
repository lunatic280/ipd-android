package com.example.ipdapplication.admin.screen

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ipdapplication.map.gcoo.dto.GcooDto
import com.example.ipdapplication.map.gcoo.viewmodel.GcooViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun AdminGcooScreen(
    vm: GcooViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by vm.state.collectAsState()
    val cameraPositionState = rememberCameraPositionState()
    val locationSource = rememberFusedLocationSource(isCompassEnabled = true)
    val context = LocalContext.current

    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedGcoo by remember { mutableStateOf<GcooDto?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        vm.loadAll()
    }

    // Toast 메시지 표시
    LaunchedEffect(state.toast) {
        state.toast?.let {
            vm.clearToast()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "킥보드(Gcoo) 관리",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
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
            if (state.tempMarker != null) {
                FloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = Color(0xFF00C46E),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "킥보드 등록"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // 지도 영역 (화면의 절반)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    NaverMap(
                        cameraPositionState = cameraPositionState,
                        locationSource = locationSource,
                        properties = MapProperties(locationTrackingMode = LocationTrackingMode.Follow),
                        uiSettings = MapUiSettings(
                            isLocationButtonEnabled = true,
                            isCompassEnabled = true,
                            isScrollGesturesEnabled = true,
                            isZoomGesturesEnabled = true,
                            isRotateGesturesEnabled = true
                        ),
                        onMapClick = { _, latLng ->
                            // 지도 클릭 시 임시 마커 설정
                            vm.setTempMarker(latLng.latitude, latLng.longitude)
                        }
                    ) {
                        // 등록된 킥보드 마커들
                        state.gcoos.forEach { gcoo ->
                            val pos = LatLng(gcoo.latitude, gcoo.longitude)
                            Marker(
                                state = rememberMarkerState(position = pos),
                                iconTintColor = Color(0xFF00C46E),
                                captionText = "${gcoo.charge}%",
                                onClick = {
                                    selectedGcoo = gcoo
                                    true
                                }
                            )
                        }

                        // 임시 마커 (새 킥보드 등록 위치)
                        state.tempMarker?.let { (lat, lng) ->
                            Marker(
                                state = rememberMarkerState(position = LatLng(lat, lng)),
                                iconTintColor = Color(0xFFFF6B6B),
                                captionText = "여기에 등록?"
                            )
                        }
                    }

                    // 지도 클릭 안내 메시지
                    if (state.tempMarker == null) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "지도를 클릭하여 킥보드 위치를 선택하세요",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // 킥보드 목록 영역 (화면의 절반)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "등록된 킥보드 목록",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (state.tempMarker != null) {
                                        TextButton(
                                            onClick = { vm.clearTempMarker() }
                                        ) {
                                            Text("선택 취소", color = Color(0xFFE53935))
                                        }
                                    }
                                }
                            }

                            if (state.gcoos.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFF5F5F5)
                                        )
                                    ) {
                                        Text(
                                            text = "등록된 킥보드가 없습니다.\n지도를 클릭하여 새 킥보드를 등록하세요.",
                                            modifier = Modifier.padding(32.dp),
                                            color = Color.Gray
                                        )
                                    }
                                }
                            } else {
                                items(state.gcoos) { gcoo ->
                                    GcooManagementCard(
                                        gcoo = gcoo,
                                        onEditClick = {
                                            selectedGcoo = gcoo
                                            showEditDialog = true
                                        },
                                        onDeleteClick = {
                                            selectedGcoo = gcoo
                                            showDeleteDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Toast 메시지 표시
            state.toast?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(message)
                }
            }
        }
    }

    // 생성 다이얼로그
    if (showCreateDialog && state.tempMarker != null) {
        CreateGcooDialog(
            latitude = state.tempMarker!!.first,
            longitude = state.tempMarker!!.second,
            context = context,
            onDismiss = {
                showCreateDialog = false
                selectedImageUri = null
            },
            onConfirm = { charge, distance, subTitle, description, userId, imageUri ->
                val imageFile = imageUri?.let { copyUriToFile(context, it, "gcoo_${System.currentTimeMillis()}") }
                vm.createGcoo(
                    charge = charge,
                    distance = distance,
                    subTitle = subTitle,
                    description = description,
                    latitude = state.tempMarker!!.first,
                    longitude = state.tempMarker!!.second,
                    userId = userId,
                    imageFile = imageFile
                )
                vm.clearTempMarker()
                showCreateDialog = false
                selectedImageUri = null
            }
        )
    }

    // 수정 다이얼로그
    if (showEditDialog && selectedGcoo != null) {
        EditGcooDialog(
            gcoo = selectedGcoo!!,
            context = context,
            onDismiss = {
                showEditDialog = false
                selectedGcoo = null
                selectedImageUri = null
            },
            onConfirm = { charge, distance, subTitle, description, latitude, longitude, userId, imageUri ->
                val imageFile = imageUri?.let { copyUriToFile(context, it, "gcoo_${System.currentTimeMillis()}") }
                selectedGcoo?.id?.let { id ->
                    vm.updateGcoo(
                        gcooId = id,
                        charge = charge,
                        distance = distance,
                        subTitle = subTitle,
                        description = description,
                        latitude = latitude,
                        longitude = longitude,
                        userId = userId,
                        imageFile = imageFile
                    )
                }
                showEditDialog = false
                selectedGcoo = null
                selectedImageUri = null
            }
        )
    }

    // 삭제 확인 다이얼로그
    if (showDeleteDialog && selectedGcoo != null) {
        DeleteGcooDialog(
            gcoo = selectedGcoo!!,
            onDismiss = {
                showDeleteDialog = false
                selectedGcoo = null
            },
            onConfirm = {
                selectedGcoo?.id?.let { id ->
                    vm.deleteGcoo(id)
                }
                showDeleteDialog = false
                selectedGcoo = null
            }
        )
    }
}

@Composable
fun GcooManagementCard(
    gcoo: GcooDto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 상단: 제목과 충전량
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = gcoo.subTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "충전량: ${gcoo.charge}% | 주행거리: ${gcoo.distance}km",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = gcoo.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // 충전량 퍼센트 표시
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        gcoo.charge >= 60 -> Color(0xFF00C46E)
                        gcoo.charge >= 30 -> Color(0xFFFFA726)
                        else -> Color(0xFFE53935)
                    }
                ) {
                    Text(
                        text = "${gcoo.charge}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 위치 정보
            Text(
                text = "위치: ${gcoo.latitude}, ${gcoo.longitude}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF00C46E)
                    )
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
    }
}

@Composable
fun CreateGcooDialog(
    latitude: Double,
    longitude: Double,
    context: Context,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String, String, Long, Uri?) -> Unit
) {
    var charge by remember { mutableStateOf("100") }
    var distance by remember { mutableStateOf("") }
    var subTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "킥보드 등록",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = subTitle,
                    onValueChange = { subTitle = it },
                    label = { Text("제목") },
                    placeholder = { Text("예) Max Plus - 389417") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("설명") },
                    placeholder = { Text("예) 분당 180원") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                OutlinedTextField(
                    value = charge,
                    onValueChange = { charge = it },
                    label = { Text("충전량(%)") },
                    placeholder = { Text("0-100") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = distance,
                    onValueChange = { distance = it },
                    label = { Text("주행 가능 거리(km)") },
                    placeholder = { Text("예) 45") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "위치: ${String.format("%.6f", latitude)}, ${String.format("%.6f", longitude)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Column {
                    Text(
                        text = "이미지",
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
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "선택한 이미지",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
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
        },
        confirmButton = {
            Button(
                onClick = {
                    if (subTitle.isNotBlank() && description.isNotBlank()) {
                        onConfirm(
                            charge.toIntOrNull() ?: 100,
                            distance.toIntOrNull() ?: 0,
                            subTitle,
                            description,
                            1L, // TODO: Get actual userId from auth
                            selectedImageUri
                        )
                    }
                },
                enabled = subTitle.isNotBlank() && description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C46E)
                )
            ) {
                Text("등록")
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
fun EditGcooDialog(
    gcoo: GcooDto,
    context: Context,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String, String, Double, Double, Long, Uri?) -> Unit
) {
    var charge by remember { mutableStateOf(gcoo.charge.toString()) }
    var distance by remember { mutableStateOf(gcoo.distance.toString()) }
    var subTitle by remember { mutableStateOf(gcoo.subTitle) }
    var description by remember { mutableStateOf(gcoo.description) }
    var latitude by remember { mutableStateOf(gcoo.latitude.toString()) }
    var longitude by remember { mutableStateOf(gcoo.longitude.toString()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "킥보드 수정",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = subTitle,
                    onValueChange = { subTitle = it },
                    label = { Text("제목") },
                    placeholder = { Text("예) Max Plus - 389417") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("설명") },
                    placeholder = { Text("예) 분당 180원") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                OutlinedTextField(
                    value = charge,
                    onValueChange = { charge = it },
                    label = { Text("충전량(%)") },
                    placeholder = { Text("0-100") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = distance,
                    onValueChange = { distance = it },
                    label = { Text("주행 가능 거리(km)") },
                    placeholder = { Text("예) 45") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("위도") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("경도") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text(
                        text = "이미지 (변경시에만 선택)",
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
                            .clickable { imagePickerLauncher.launch("image/*") },
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
                            !gcoo.imageUrl.isNullOrBlank() -> {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(gcoo.imageUrl)
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
                onClick = {
                    onConfirm(
                        charge.toIntOrNull() ?: 0,
                        distance.toIntOrNull() ?: 0,
                        subTitle,
                        description,
                        latitude.toDoubleOrNull() ?: 0.0,
                        longitude.toDoubleOrNull() ?: 0.0,
                        gcoo.userId,
                        selectedImageUri
                    )
                },
                enabled = subTitle.isNotBlank() && description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C46E)
                )
            ) {
                Text("수정")
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
fun DeleteGcooDialog(
    gcoo: GcooDto,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "킥보드 삭제",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "${gcoo.subTitle}을(를) 정말 삭제하시겠습니까?\n\n이 작업은 되돌릴 수 없습니다."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935)
                )
            ) {
                Text("삭제")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}


// URI를 File로 변환하는 헬퍼 함수
private fun copyUriToFile(context: Context, uri: Uri, prefix: String): File? {
    return runCatching {
        val resolver = context.contentResolver
        val ext = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(resolver.getType(uri)) ?: "jpg"
        val file = File.createTempFile(prefix, ".$ext", context.cacheDir)

        resolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: return null
        file
    }.getOrNull()
}
