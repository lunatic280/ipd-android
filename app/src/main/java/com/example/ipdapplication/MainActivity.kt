package com.example.ipdapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.admin.screen.AdminGcooScreen
import com.example.ipdapplication.admin.screen.AdminMenuScreen
import com.example.ipdapplication.admin.screen.AdminPlaceScreen
import com.example.ipdapplication.admin.screen.AdminScreen
import com.example.ipdapplication.auth.ui.LoginScreen
import com.example.ipdapplication.auth.ui.SignUpScreen
import com.example.ipdapplication.community.screen.BlogDetailScreen
import com.example.ipdapplication.community.screen.CommunityScreen
import com.example.ipdapplication.community.screen.CommunityWriteScreen
import com.example.ipdapplication.cupon.screen.AdminCouponIssueScreen
import com.example.ipdapplication.cupon.screen.CuponScreen
import com.example.ipdapplication.led.screen.LedControlScreen
import com.example.ipdapplication.main.MainScreen
import com.example.ipdapplication.map.screen.LocationTrackingWithMarkers
import com.example.ipdapplication.map.screen.MenuAllScreen
import com.example.ipdapplication.map.screen.review.ReviewListScreen
import com.example.ipdapplication.map.screen.review.ReviewWriteData
import com.example.ipdapplication.map.screen.review.ReviewWriteScreen
import com.example.ipdapplication.map.review.viewmodel.ReviewModel
import com.example.ipdapplication.qr.screen.QrControlScreen
import com.example.ipdapplication.ui.theme.IpdApplicationTheme
import com.example.ipdapplication.user.screen.SplashScreen
import com.example.ipdapplication.user.screen.UserHomeScreen
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            IpdApplicationTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "login"   // ???�작 ??로그???�면
        ) {
            // 로그??
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { _, role ->
                        val normalizedRole = role.trim().uppercase().removePrefix("ROLE_")
                        // ??��???�라 ?�른 ?�면?�로 ?�동
                        if (normalizedRole == "OWNER") {
                            // 관리자(OWNER): 기존 메인 ?�면
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            // ?�반 ?�용?? ?�플?�시 ?�면?�로 ?�동
                            navController.navigate("user_splash") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate("signup")
                    }
                )
            }

            // ?�반 ?�용???�플?�시 ?�면 (img.png)
            composable("user_splash") {
                SplashScreen(
                    onTimeout = {
                        navController.navigate("user_home") {
                            popUpTo("user_splash") { inclusive = true }
                        }
                    }
                )
            }

            // ?�반 ?�용?????�면 (img_1.png)
            composable("user_home") {
                UserHomeScreen(
                    onNavigateToMap = { navController.navigate("user_map") },
                    onNavigateToQR = { navController.navigate("qr") },
                    onNavigateToCoupon = { navController.navigate("coupon") }
                )
            }

            // ?�반 ?�용?�용 지???�면 (배터�?교체?�기 버튼 ?�함)
            composable("user_map") { backStackEntry ->
                val reviewCreated = backStackEntry.savedStateHandle.get<Boolean>("reviewCreated") ?: false

                // 리뷰가 작성되면 플래그 제거
                LaunchedEffect(reviewCreated) {
                    if (reviewCreated) {
                        backStackEntry.savedStateHandle.remove<Boolean>("reviewCreated")
                    }
                }

                LocationTrackingWithMarkers(
                    onNavigateToReviewWrite = { placeId, placeName ->
                        navController.navigate("review/write/$placeId/$placeName")
                    },
                    onNavigateToMenuAll = { placeId, placeName ->
                        navController.navigate("menu/all/$placeId/$placeName")
                    },
                    onNavigateToReviewAll = { placeId, placeName ->
                        navController.navigate("review/list/$placeId/$placeName")
                    },
                    onNavigateToQR = { navController.navigate("qr") },
                    refreshReviews = reviewCreated
                )
            }

            // ?�원가??
            composable("signup") {
                SignUpScreen(
                    onSignUpSuccess = {
                        // ?�원가???�료 ??로그???�면?�로 ?�아�?
                        navController.popBackStack()
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // 메인 ?�면
            composable("main") {
                MainScreen(
                    onOpenMap = { navController.navigate("map") },
                    onOpenCommunity = { navController.navigate("community") },
                    onOpenCoupon = { navController.navigate("coupon") },
                    onOpenQR = { navController.navigate("qr") },
                    onOpenAdmin = { navController.navigate("admin") }
                )
            }

            // 쿠폰 ?�면
            composable("coupon") {
                CuponScreen()
            }

            // 지???�면
            composable("map") { backStackEntry ->
                val reviewCreated = backStackEntry.savedStateHandle.get<Boolean>("reviewCreated") ?: false

                LaunchedEffect(reviewCreated) {
                    if (reviewCreated) {
                        backStackEntry.savedStateHandle.remove<Boolean>("reviewCreated")
                    }
                }

                LocationTrackingWithMarkers(
                    onNavigateToReviewWrite = { placeId, placeName ->
                        navController.navigate("review/write/$placeId/$placeName")
                    },
                    onNavigateToMenuAll = { placeId, placeName ->
                        navController.navigate("menu/all/$placeId/$placeName")
                    },
                    onNavigateToReviewAll = { placeId, placeName ->
                        navController.navigate("review/list/$placeId/$placeName")
                    },
                    refreshReviews = reviewCreated
                )
            }

            // 리뷰 ?�성 ?�면
            composable(
                route = "review/write/{placeId}/{placeName}",
                arguments = listOf(
                    navArgument("placeId") { type = NavType.LongType },
                    navArgument("placeName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getLong("placeId") ?: 0L
                val placeName = backStackEntry.arguments?.getString("placeName") ?: ""
                val reviewViewModel: ReviewModel = hiltViewModel()

                ReviewWriteScreen(
                    placeName = placeName,
                    visitCount = 1,
                    onClose = { navController.popBackStack() },
                    onSubmit = { reviewData ->
                        reviewViewModel.createReview(
                            placeId = placeId,
                            reviewComment = reviewData.placeReview,
                            bssComment = reviewData.bssReview,
                            userId = 1L,
                            tagIds = reviewData.placeTagIds + reviewData.bssTagIds,
                            imageFiles = reviewData.photoUris.mapIndexedNotNull { idx, uri ->
                                copyUriToFile(context, uri, "review_${placeId}_$idx")
                            }
                        )
                        // 결과를 이전 화면에 전달
                        navController.previousBackStackEntry?.savedStateHandle?.set("reviewCreated", true)
                        navController.popBackStack()
                    },
                    reviewViewModel = reviewViewModel
                )
            }

            // 메뉴 ?�체보기 ?�면
            composable(
                route = "review/list/{placeId}/{placeName}",
                arguments = listOf(
                    navArgument("placeId") { type = NavType.LongType },
                    navArgument("placeName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getLong("placeId") ?: 0L
                val placeName = backStackEntry.arguments?.getString("placeName") ?: ""

                ReviewListScreen(
                    placeId = placeId,
                    placeName = placeName,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = "menu/all/{placeId}/{placeName}",
                arguments = listOf(
                    navArgument("placeId") { type = NavType.LongType },
                    navArgument("placeName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getLong("placeId") ?: 0L
                val placeName = backStackEntry.arguments?.getString("placeName") ?: ""

                MenuAllScreen(
                    placeId = placeId,
                    placeName = placeName,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // QR ?�면
            composable("qr") {
                QrControlScreen(
                    onBackToMain = {
                        // QR ?�면?�서 ?�공 ??메인?�로 복�?
                        navController.popBackStack()
                    }
                )
            }

            // 커�??�티 리스???�면
            composable("community") {
                CommunityScreen(
                    onWriteClick = { navController.navigate("community/write") },
                    // ???�기 ?�름??onBlogClick ???�니??onPostClick ?�로 맞춰????
                    onPostClick = { blogId ->
                        navController.navigate("community/detail/$blogId")
                    }
                )
            }

            // 커�??�티 글 ?�성 ?�면
            composable("community/write") {
                CommunityWriteScreen(
                    // ??컴포?��??�그?�처가 onBack ?��?�?onBackClick ???�니??onBack ?�용
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // 커�??�티 글 ?�세 ?�면
            composable(
                route = "community/detail/{blogId}",
                arguments = listOf(
                    navArgument("blogId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val blogId = backStackEntry.arguments?.getLong("blogId") ?: 0L

                BlogDetailScreen(
                    blogId = blogId,
                    // ???�기??onBackClick ??onBack ?�로 ?�일
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // 관리자 메인 화면
            composable("admin") {
                AdminScreen(
                    onBackClick = { navController.popBackStack() },
                    onPlaceManageClick = { navController.navigate("admin/place") },
                    onMenuManageClick = { navController.navigate("admin/menu") },
                    onGcooManageClick = { navController.navigate("admin/gcoo") },
                    onLedControlClick = { navController.navigate("admin/led") },
                    onCouponManageClick = { navController.navigate("admin/coupon") }
                )
            }

            // 관리자 Place ?�록 ?�면
            composable("admin/place") {
                AdminPlaceScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 관리자 Menu 등록 화면
            composable("admin/menu") {
                AdminMenuScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 관리자 Gcoo(킥보드) 관리 화면
            composable("admin/gcoo") {
                AdminGcooScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 관리자 LED 제어 화면
            composable("admin/led") {
                LedControlScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 관리자 쿠폰 발급/관리 화면
            composable("admin/coupon") {
                AdminCouponIssueScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

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
