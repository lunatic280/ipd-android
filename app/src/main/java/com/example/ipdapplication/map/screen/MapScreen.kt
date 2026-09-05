package com.example.ipdapplication.map.screen

import android.content.res.Configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.R
import com.example.ipdapplication.map.menu.viewmodel.MenuViewModel
import com.example.ipdapplication.map.review.viewmodel.ReviewModel
import com.example.ipdapplication.map.screen.Button.RouteStatusBadge
import com.example.ipdapplication.map.gcoo.screen.GcooInfoCard
import com.example.ipdapplication.map.screen.place.BatterySwapButton
import com.example.ipdapplication.map.screen.place.PlaceDetailSheet
import com.example.ipdapplication.map.screen.place.PlaceHeaderSection
import com.example.ipdapplication.map.screen.place.PlaceInfoTexts
import com.example.ipdapplication.map.screen.place.SavePlaceBottomBar
import com.example.ipdapplication.map.viewModel.PlaceViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch

// 밀리초를 "n시간 nn분" 형식으로 변환
private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}시간 ${minutes}분"
        hours > 0 -> "${hours}시간"
        minutes > 0 -> "${minutes}분"
        else -> "1분 미만"
    }
}

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    vm: PlaceViewModel = hiltViewModel(),
    menuViewModel: MenuViewModel = hiltViewModel(),
    reviewViewModel: ReviewModel = hiltViewModel(),
    // 사용자 모드: 기본값 false (등록 기능 비활성)
    // 관리자/운영자라면 true 값으로 호출해서 등록 기능 사용
    canCreatePlace: Boolean = false,
    onNavigateToReviewWrite: (placeId: Long, placeName: String) -> Unit = { _, _ -> },
    onNavigateToMenuAll: (placeId: Long, placeName: String) -> Unit = { _, _ -> },
    onNavigateToReviewAll: (placeId: Long, placeName: String) -> Unit = { _, _ -> },
    onNavigateToQR: () -> Unit = {},
    refreshReviews: Boolean = false
) {
    val state by vm.state.collectAsState()
    val menuState by menuViewModel.state.collectAsState()
    val reviewState by reviewViewModel.state.collectAsState()

    // 화면 방향 / 폼팩터 감지
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val screenWidthDp = configuration.screenWidthDp
    val isTablet = screenWidthDp >= 600

    val cameraPositionState = rememberCameraPositionState()
    val locationSource = rememberFusedLocationSource(isCompassEnabled = true)
    val sheetScrollState = rememberScrollState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isExpanded = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded

    // 경로 관련 로컬 스테이트
    var startPoint by remember { mutableStateOf<LatLng?>(null) }
    var goalPoint by remember { mutableStateOf<LatLng?>(null) }
    var routePath by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var isRequestingRoute by remember { mutableStateOf(false) }
    var routeMessage by remember { mutableStateOf<String?>(null) }

    // 출발/도착 선택 모드
    var isSelectingDestination by remember { mutableStateOf(false) }
    var selectedStartMarker by remember { mutableStateOf<LatLng?>(null) }

    // 사용자 등록 모드
    var isUserCreateMode by remember { mutableStateOf(false) }
    val allowCreate = canCreatePlace || isUserCreateMode

    val scope = rememberCoroutineScope()

    // place 목록 로딩
    LaunchedEffect(Unit) {
        vm.loadAll()
    }

    // 선택 place 변경/리뷰 refresh 시 메뉴/리뷰 재로딩
    LaunchedEffect(state.selected?.id, refreshReviews) {
        state.selected?.id?.let { placeId ->
            reviewViewModel.loadReviewsForPlace(placeId)
            menuViewModel.loadMenusByPlaceId(placeId)
        } ?: run {
            menuViewModel.clearMenus()
        }
    }

    // 시트 기본 높이
    val sheetPeekHeight = if (state.selected == null && state.selectedGcoo == null) {
        0.dp
    } else {
        when {
            isLandscape && isTablet -> 220.dp
            isLandscape -> 180.dp
            isTablet -> 320.dp
            else -> 280.dp
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetDragHandle = {
            // 커스텀 드래그 핸들 (선택된 항목이 있을 때만 표시)
            if (state.selected != null || state.selectedGcoo != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(5.dp)
                            .background(
                                color = Color(0xFFBDBDBD),
                                shape = RoundedCornerShape(2.5.dp)
                            )
                    )
                }
            }
        },
        sheetContainerColor = Color.White,
        sheetMaxWidth = screenWidthDp.dp,
        sheetContent = {
            state.selected?.let { sel ->
                val placePos = LatLng(sel.latitude, sel.longitude)
                PlaceDetailSheet(
                    place = sel,
                    comments = state.comments,
                    isExpanded = isExpanded,
                    scrollState = sheetScrollState,
                    placeLike = state.selectedPlaceLike,
                    commentLikeMap = state.commentLikeMap,
                    onTogglePlaceLike = { vm.togglePlaceLike() },
                    onToggleCommentLike = { commentId -> vm.toggleCommentLike(commentId) },
                    location = state.selectedLocationText,
                    menus = menuState.menus,
                    reviews = reviewState.reviews,
                    onWriteReviewClick = { sel.id?.let { onNavigateToReviewWrite(it, sel.title) } },
                    onMenuViewAllClick = { sel.id?.let { onNavigateToMenuAll(it, sel.title) } },
                    onReviewViewAllClick = { sel.id?.let { onNavigateToReviewAll(it, sel.title) } },
                    onDepartureClick = {
                        // 출발 버튼: 이 장소를 출발지로 설정하고 도착지 선택 모드
                        selectedStartMarker = placePos
                        startPoint = placePos
                        goalPoint = null
                        isSelectingDestination = true
                        routeMessage = "도착할 마커를 클릭하세요"
                        routePath = emptyList()
                        vm.clearSelection()
                    },
                    onArrivalClick = {
                        // 도착 버튼: 현재 위치에서 이 장소로 경로 추천
                        val currentLocation = cameraPositionState.position.target
                        goalPoint = placePos
                        startPoint = currentLocation
                        isSelectingDestination = false
                        isRequestingRoute = true
                        routeMessage = "경로를 불러오는 중..."
                        routePath = emptyList()
                        scope.launch {
                            try {
                                val route = vm.requestRoute(
                                    startLat = currentLocation.latitude,
                                    startLng = currentLocation.longitude,
                                    goalLat = placePos.latitude,
                                    goalLng = placePos.longitude
                                )
                                routePath = route.path.map { LatLng(it.lat, it.lng) }
                                routeMessage = "약 ${route.distance}m, 약 ${formatDuration(route.duration)}"
                            } catch (e: Exception) {
                                routeMessage = "경로 조회에 실패했습니다."
                            } finally {
                                isRequestingRoute = false
                                // 경로 추천 후 출발지/도착지 초기화
                                startPoint = null
                                goalPoint = null
                                selectedStartMarker = null
                            }
                        }
                        vm.clearSelection()
                    }
                )
            } ?: state.selectedGcoo?.let { gcoo ->
                GcooInfoCard(
                    gcoo = gcoo,
                    onReserveClick = { onNavigateToQR() },
                    onCloseClick = { vm.clearGcooSelection() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
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
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp),
                onMapClick = { _, latLng ->
                    goalPoint = latLng
                    routePath = emptyList()
                    routeMessage = "목적지가 설정되었습니다"

                    if (allowCreate) {
                        vm.setTempMarker(latLng.latitude, latLng.longitude)
                    } else {
                        vm.clearTemp()
                    }

                    vm.clearSelection()
                    vm.clearGcooSelection()
                }
            ) {
                if (state.showPlaces) {
                    state.places.forEach { p ->
                        val pos = LatLng(p.latitude, p.longitude)
                        Marker(
                            state = rememberMarkerState(position = pos),
                            icon = OverlayImage.fromResource(R.drawable.store_marker),
                            width = 48.dp,
                            height = 56.dp,
                            captionText = if (state.selected?.id == p.id) p.title else "",
                            onClick = {
                                if (isSelectingDestination && selectedStartMarker != null) {
                                    // 도착지 선택 모드: 경로 추천 API 호출
                                    goalPoint = pos
                                    isSelectingDestination = false
                                    isRequestingRoute = true
                                    routeMessage = "경로를 불러오는 중..."
                                    routePath = emptyList()
                                    scope.launch {
                                        try {
                                            val route = vm.requestRoute(
                                                startLat = selectedStartMarker!!.latitude,
                                                startLng = selectedStartMarker!!.longitude,
                                                goalLat = pos.latitude,
                                                goalLng = pos.longitude
                                            )
                                            routePath = route.path.map { LatLng(it.lat, it.lng) }
                                            routeMessage = "약 ${route.distance}m, 약 ${formatDuration(route.duration)}"
                                        } catch (e: Exception) {
                                            routeMessage = "경로 조회에 실패했습니다."
                                        } finally {
                                            isRequestingRoute = false
                                            // 경로 추천 후 출발지/도착지 초기화
                                            startPoint = null
                                            goalPoint = null
                                            selectedStartMarker = null
                                        }
                                    }
                                } else {
                                    vm.selectPlace(p)
                                    vm.clearGcooSelection()
                                }
                                true
                            }
                        )
                    }
                }

                state.gcoos.forEach { gcoo ->
                    val pos = LatLng(gcoo.latitude, gcoo.longitude)
                    Marker(
                        state = rememberMarkerState(position = pos),
                        icon = OverlayImage.fromResource(R.drawable.mobility_marker),
                        width = 48.dp,
                        height = 50.dp,
                        captionText = if (state.selectedGcoo?.id == gcoo.id) "${gcoo.charge}%" else "",
                        onClick = {
                            if (isSelectingDestination && selectedStartMarker != null) {
                                // 도착지 선택 모드: 경로 추천 API 호출
                                goalPoint = pos
                                isSelectingDestination = false
                                isRequestingRoute = true
                                routeMessage = "경로를 불러오는 중..."
                                routePath = emptyList()
                                scope.launch {
                                    try {
                                        val route = vm.requestRoute(
                                            startLat = selectedStartMarker!!.latitude,
                                            startLng = selectedStartMarker!!.longitude,
                                            goalLat = pos.latitude,
                                            goalLng = pos.longitude
                                        )
                                        routePath = route.path.map { LatLng(it.lat, it.lng) }
                                        routeMessage = "약 ${route.distance}m, 약 ${formatDuration(route.duration)}"
                                    } catch (e: Exception) {
                                        routeMessage = "경로 조회에 실패했습니다."
                                    } finally {
                                        isRequestingRoute = false
                                        // 경로 추천 후 출발지/도착지 초기화
                                        startPoint = null
                                        goalPoint = null
                                        selectedStartMarker = null
                                    }
                                }
                            } else {
                                vm.selectGcoo(gcoo)
                            }
                            true
                        }
                    )
                }

                if (allowCreate) {
                    state.tempMarker?.let { (lat, lng) ->
                        Marker(
                            state = rememberMarkerState(position = LatLng(lat, lng)),
                            iconTintColor = Color(0xFF00C853),
                            captionText = "임시 위치"
                        )
                    }
                }

                if (routePath.isNotEmpty()) {
                    PathOverlay(
                        coords = routePath,
                        color = Color(0xFF00C46E),
                        width = 6.dp
                    )
                }
            }

            RouteStatusBadge(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = if (isLandscape) 8.dp else 16.dp),
                startPoint = startPoint,
                goalPoint = goalPoint,
                message = routeMessage
            )

            // 경로 선택 모드 안내 바
            if (isSelectingDestination) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 100.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF00C46E),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "도착지를 선택하세요",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "지도의 마커를 터치해주세요",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        Button(
                            onClick = {
                                isSelectingDestination = false
                                selectedStartMarker = null
                                startPoint = null
                                goalPoint = null
                                routeMessage = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF00C46E)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "취소",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { vm.toggleShowPlaces() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = if (isLandscape) 8.dp else 16.dp, end = 16.dp)
                    .size(68.dp),
                containerColor = if (state.showPlaces) Color(0xFF00C46E) else Color(0xFFE53935),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "장소",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (state.showPlaces) "ON" else "OFF",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (allowCreate && state.selected == null && state.tempMarker != null) {
                SavePlaceBottomBar(
                    onSave = { title, desc, tel, type -> vm.savePlace(title, desc, tel, type) },
                    onCancel = { vm.clearTemp() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .imePadding()
                )
            }

            // Gcoo 카드/버튼 묶음 혹은 기본 FAB 배치
            val fabBasePadding = when {
                isLandscape && isTablet -> 24.dp
                isLandscape -> 16.dp
                isTablet -> 32.dp
                else -> 24.dp
            }
            val fabExtraBottom = when {
                allowCreate && state.selected == null && state.tempMarker != null ->
                    if (isLandscape) 80.dp else 160.dp
                else -> 0.dp
            }
            val fabBottomPadding = fabBasePadding + fabExtraBottom

            if (state.selected == null && state.selectedGcoo == null) {
                BatterySwapButton(
                    onClick = onNavigateToQR,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            start = fabBasePadding,
                            bottom = fabBottomPadding
                        )
                )

                if (canCreatePlace) {
                    FloatingActionButton(
                        onClick = {
                            val newUserMode = !isUserCreateMode
                            isUserCreateMode = newUserMode
                            if (!(canCreatePlace || newUserMode)) {
                                vm.clearTemp()
                            }
                        },
                        containerColor = if (allowCreate) Color(0xFF00C46E) else Color(0xFFBDBDBD),
                        contentColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                end = fabBasePadding,
                                bottom = fabBottomPadding
                            )
                    ) {
                        Text(
                            text = if (allowCreate) "마커 등록 ON" else "마커 등록 OFF",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * LocationTrackingWithMarkers 래퍼
 */
@Composable
fun LocationTrackingWithMarkers(
    vm: PlaceViewModel = hiltViewModel(),
    menuViewModel: MenuViewModel = hiltViewModel(),
    reviewViewModel: ReviewModel = hiltViewModel(),
    canCreatePlace: Boolean = false,
    onNavigateToReviewWrite: (placeId: Long, placeName: String) -> Unit = { _, _ -> },
    onNavigateToMenuAll: (placeId: Long, placeName: String) -> Unit = { _, _ -> },
    onNavigateToReviewAll: (placeId: Long, placeName: String) -> Unit = { _, _ -> },
    onNavigateToQR: () -> Unit = {},
    refreshReviews: Boolean = false
) {
    MapScreen(
        vm = vm,
        menuViewModel = menuViewModel,
        reviewViewModel = reviewViewModel,
        canCreatePlace = canCreatePlace,
        onNavigateToReviewWrite = onNavigateToReviewWrite,
        onNavigateToMenuAll = onNavigateToMenuAll,
        onNavigateToReviewAll = onNavigateToReviewAll,
        onNavigateToQR = onNavigateToQR,
        refreshReviews = refreshReviews
    )
}
