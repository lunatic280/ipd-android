package com.example.ipdapplication.admin.screen

import android.content.Context
import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ipdapplication.map.dto.PlaceDto
import com.example.ipdapplication.map.viewModel.PlaceViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.compose.rememberMarkerState
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun AdminPlaceScreen(
    vm: PlaceViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by vm.state.collectAsState()
    val cameraPositionState = rememberCameraPositionState()
    val locationSource = rememberFusedLocationSource(isCompassEnabled = true)
    val context = LocalContext.current

    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImageUris = uris ?: emptyList()
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var telNumber by remember { mutableStateOf("") }
    var placeType by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var editingPlace by remember { mutableStateOf<PlaceDto?>(null) }

    LaunchedEffect(Unit) {
        vm.loadAll()
    }

    LaunchedEffect(state.toast) {
        state.toast?.let {
            // Placeholder for future toast handling
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "\uad00\ub9ac\uc790 Place",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "\ub4a4\ub85c \uac00\uae30"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C46E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    vm.clearSelection()
                    vm.setTempMarker(latLng.latitude, latLng.longitude)
                    selectedImageUris = emptyList()
                    showDialog = true
                }
            ) {
                state.places.forEach { p ->
                    val pos = LatLng(p.latitude, p.longitude)
                    Marker(
                        state = rememberMarkerState(position = pos),
                        captionText = p.title,
                        onClick = {
                            vm.selectPlace(p)
                            showDialog = false
                            true
                        }
                    )
                }

                state.tempMarker?.let { (lat, lng) ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(lat, lng)),
                        iconTintColor = Color(0xFF00C853),
                        captionText = "\uc784\uc2dc \uc704\uce58"
                    )
                }
            }

            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "\uc9c0\ub3c4\uc5d0\uc11c \uc704\uce58\ub97c \uace7\uc774 \ud074\ub9ad\ud574 Place\ub97c \ub4f1\ub85d\ud558\uc138\uc694.",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                )
            }

            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF00C46E)
                )
            }

            state.selected?.let { selected ->
                val mainImage = selected.imageUrls.firstOrNull() ?: selected.imageUrl
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (!mainImage.isNullOrBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(mainImage)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "${selected.title} image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selected.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A)
                            )
                            TextButton(onClick = { vm.clearSelection() }) {
                                Text("\ub2eb\uae30", color = Color.Gray)
                            }
                        }
                        Text(text = "\uc124\uba85: ${selected.description ?: "-"}")
                        Text(text = "\uc804\ud654\ubc88\ud638: ${selected.telNumber ?: "-"}")
                        Text(text = "\ud0c0\uc785: ${selected.type ?: "-"}")
                        Text(text = "\ub4f1\ub85d\uc77c: ${formatCreatedAt(selected.createdAt)}")
                        Text(
                            text = "\uc88c\ud45c: ${formatCoordinate(selected.latitude)}, ${formatCoordinate(selected.longitude)}"
                        )
                        Text(text = "\uc88b\uc544\uc694: ${selected.likeCount}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    editingPlace = selected
                                    title = selected.title
                                    description = selected.description ?: ""
                                    telNumber = selected.telNumber ?: ""
                                    placeType = selected.type ?: ""
                                    selectedImageUris = emptyList()
                                    showEditDialog = true
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00C46E)
                                )
                            ) {
                                Text("\uc218\uc815")
                            }
                            Button(
                                onClick = {
                                    editingPlace = selected
                                    showDeleteDialog = true
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF5252)
                                )
                            ) {
                                Text("\uc0ad\uc81c")
                            }
                        }
                    }
                }
            }

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

        if (showDialog && state.tempMarker != null) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    vm.clearTemp()
                    selectedImageUris = emptyList()
                },
                title = {
                    Text(
                        text = "Place \ub4f1\ub85d",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "\uc120\ud0dd\ud55c \uc704\uce58\uc5d0 Place\ub97c \ub4f1\ub85d\ud569\ub2c8\ub2e4.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("\uc7a5\uc18c \uc774\ub984") },
                            placeholder = { Text("\uc608) \ub9db\uc9d1 \uc774\ub984") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("\uc124\uba85 (\uc120\ud0dd)") },
                            placeholder = { Text("\uc7a5\uc18c\uc5d0 \ub300\ud55c \uc124\uba85\uc744 \uc785\ub825\ud558\uc138\uc694") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4
                        )

                        OutlinedTextField(
                            value = telNumber,
                            onValueChange = { telNumber = it },
                            label = { Text("\uc804\ud654\ubc88\ud638") },
                            placeholder = { Text("000-0000-0000") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = placeType,
                            onValueChange = { placeType = it },
                            label = { Text("\uc704\uce58 \ud0c0\uc785") },
                            placeholder = { Text("\uc608) \uc2dd\ub2f9, \uce74\ud398") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "\ub300\ud45c \uc774\ubbf8\uc9c0 (\uc5ec\ub7ec \uc7a5 \uc120\ud0dd \uac00\ub2a5)",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(selectedImageUris) { uri ->
                                    Box(
                                        modifier = Modifier
                                            .size(110.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(uri)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "선택한 Place 이미지",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        TextButton(
                                            onClick = {
                                                selectedImageUris =
                                                    selectedImageUris.filterNot { it == uri }
                                            },
                                            modifier = Modifier.align(Alignment.TopEnd)
                                        ) {
                                            Text("삭제", color = Color.White, fontSize = 10.sp)
                                        }
                                    }
                                }

                                item {
                                    Box(
                                        modifier = Modifier
                                            .size(110.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { imagePickerLauncher.launch("image/*") },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "사진 추가",
                                            color = Color.Gray,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            if (selectedImageUris.isEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "사진을 선택하지 않으면 이미지 없이 등록됩니다.",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (title.isNotBlank() && telNumber.isNotBlank() && placeType.isNotBlank()) {
                                val imageFiles = selectedImageUris.mapNotNull { uri -> uriToFile(context, uri) }
                                vm.savePlace(
                                    title,
                                    description.ifBlank { null },
                                    telNumber,
                                    placeType,
                                    imageFiles
                                )
                                showDialog = false
                                title = ""
                                description = ""
                                telNumber = ""
                                placeType = ""
                                selectedImageUris = emptyList()
                            }
                        },
                        enabled = title.isNotBlank() && telNumber.isNotBlank() && placeType.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00C46E)
                        )
                    ) {
                        Text("\ub4f1\ub85d")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            vm.clearTemp()
                            title = ""
                            description = ""
                            telNumber = ""
                            placeType = ""
                            selectedImageUris = emptyList()
                        }
                    ) {
                        Text("\ucde8\uc18c", color = Color.Gray)
                    }
                }
            )
        }

        if (showEditDialog && editingPlace != null) {
            val target = editingPlace!!
            AlertDialog(
                onDismissRequest = {
                    showEditDialog = false
                    editingPlace = null
                    title = ""
                    description = ""
                    telNumber = ""
                    placeType = ""
                },
                title = { Text(text = "Place \uc218\uc815", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "\uc120\ud0dd\ud55c Place \uc815\ubcf4\ub97c \uc218\uc815\ud569\ub2c8\ub2e4.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("\uc7a5\uc18c \uc774\ub984") },
                            placeholder = { Text("\uc608) \ub9db\uc9d1 \uc774\ub984") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("\uc124\uba85 (\uc120\ud0dd)") },
                            placeholder = { Text("\uc7a5\uc18c\uc5d0 \ub300\ud55c \uc124\uba85\uc744 \uc785\ub825\ud558\uc138\uc694") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4
                        )
                        OutlinedTextField(
                            value = telNumber,
                            onValueChange = { telNumber = it },
                            label = { Text("\uc804\ud654\ubc88\ud638") },
                            placeholder = { Text("000-0000-0000") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = placeType,
                            onValueChange = { placeType = it },
                            label = { Text("\uc704\uce58 \ud0c0\uc785") },
                            placeholder = { Text("\uc608) \uc2dd\ub2f9, \uce74\ud398") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (title.isNotBlank() && telNumber.isNotBlank() && placeType.isNotBlank()) {
                                vm.updatePlace(
                                    id = target.id ?: return@Button,
                                    title = title,
                                    description = description.ifBlank { null },
                                    telNumber = telNumber,
                                    type = placeType,
                                    latitude = target.latitude,
                                    longitude = target.longitude
                                )
                                showEditDialog = false
                                editingPlace = null
                                title = ""
                                description = ""
                                telNumber = ""
                                placeType = ""
                            }
                        },
                        enabled = title.isNotBlank() && telNumber.isNotBlank() && placeType.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C46E))
                    ) {
                        Text("\uc218\uc815")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showEditDialog = false
                            editingPlace = null
                            title = ""
                            description = ""
                            telNumber = ""
                            placeType = ""
                        }
                    ) {
                        Text("\ucde8\uc18c", color = Color.Gray)
                    }
                }
            )
        }

        if (showDeleteDialog && editingPlace != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    editingPlace = null
                },
                title = { Text(text = "Place \uc0ad\uc81c", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        text = "\"${editingPlace?.title}\" \ud56d\ubaa9\uc744 \uc0ad\uc81c\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c?\n\uc0ad\uc81c \ud6c4 \ubcf5\uad6c\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.",
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            editingPlace?.id?.let { vm.deletePlace(it) }
                            showDeleteDialog = false
                            editingPlace = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                    ) {
                        Text("\uc0ad\uc81c")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            editingPlace = null
                        }
                    ) {
                        Text("\ucde8\uc18c", color = Color.Gray)
                    }
                }
            )
        }
    }
}

private fun formatCreatedAt(raw: String?): String {
    if (raw.isNullOrBlank()) return "-"
    return raw.replace("T", " ").substringBefore(".")
}

private fun formatCoordinate(value: Double): String = String.format("%.5f", value)

private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "place_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        file
    } catch (e: Exception) {
        null
    }
}
