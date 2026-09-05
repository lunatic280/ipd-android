package com.example.ipdapplication.qr.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview as ComposePreview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun QrScanScreen(
    onQrScanned: (String) -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        if (hasCameraPermission) {
            QrCameraPreviewWithOverlay(
                onQrScanned = onQrScanned,
                lifecycleOwner = lifecycleOwner
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "카메라 권한이 필요합니다.",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00C46E)
                        )
                    ) {
                        Text("권한 허용")
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "배터리 스테이션 QR을 스캔해주세요",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "프레임 안에 QR을 맞추면 자동으로 인식돼요",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF1A1A1A).copy(alpha = 0.8f),
                            Color(0xFF1A1A1A)
                        )
                    )
                )
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp, bottom = 32.dp)
        ) {
            Button(
                onClick = onSkipClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C46E)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "건너뛰기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun QrCameraPreviewWithOverlay(
    onQrScanned: (String) -> Unit,
    lifecycleOwner: LifecycleOwner
) {
    val context = LocalContext.current
    var hasScanned by remember { mutableStateOf(false) }

    val barcodeScanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }

    val previewView = remember { PreviewView(context) }

    DisposableEffect(lifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                        if (!hasScanned) {
                            @Suppress("UnsafeOptInUsageError")
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val inputImage = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                barcodeScanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        barcodes.firstOrNull()?.rawValue?.let { qrData ->
                                            hasScanned = true
                                            onQrScanned(qrData)
                                        }
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            cameraProviderFuture.get().unbindAll()
            barcodeScanner.close()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        StationQrScanOverlay()
    }
}

@Composable
private fun StationQrScanOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val scanBoxWidth = 280.dp.toPx()
        val scanBoxHeight = 200.dp.toPx()
        val left = (size.width - scanBoxWidth) / 2
        val top = (size.height - scanBoxHeight) / 2 - 40.dp.toPx()
        val cornerRadius = 16.dp.toPx()
        val cornerLength = 40.dp.toPx()
        val strokeWidth = 4.dp.toPx()

        drawRect(
            color = Color.Black.copy(alpha = 0.6f),
            size = size
        )

        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(scanBoxWidth, scanBoxHeight),
            cornerRadius = CornerRadius(cornerRadius),
            blendMode = BlendMode.Clear
        )

        val cornerColor = Color(0xFF00C46E)

        drawLine(
            color = cornerColor,
            start = Offset(left, top + cornerRadius),
            end = Offset(left, top + cornerLength),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(left + cornerRadius, top),
            end = Offset(left + cornerLength, top),
            strokeWidth = strokeWidth
        )

        drawLine(
            color = cornerColor,
            start = Offset(left + scanBoxWidth, top + cornerRadius),
            end = Offset(left + scanBoxWidth, top + cornerLength),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(left + scanBoxWidth - cornerRadius, top),
            end = Offset(left + scanBoxWidth - cornerLength, top),
            strokeWidth = strokeWidth
        )

        drawLine(
            color = cornerColor,
            start = Offset(left, top + scanBoxHeight - cornerRadius),
            end = Offset(left, top + scanBoxHeight - cornerLength),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(left + cornerRadius, top + scanBoxHeight),
            end = Offset(left + cornerLength, top + scanBoxHeight),
            strokeWidth = strokeWidth
        )

        drawLine(
            color = cornerColor,
            start = Offset(left + scanBoxWidth, top + scanBoxHeight - cornerRadius),
            end = Offset(left + scanBoxWidth, top + scanBoxHeight - cornerLength),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(left + scanBoxWidth - cornerRadius, top + scanBoxHeight),
            end = Offset(left + scanBoxWidth - cornerLength, top + scanBoxHeight),
            strokeWidth = strokeWidth
        )
    }
}

@ComposePreview(showBackground = true)
@Composable
private fun QrScanScreenPreview() {
    QrScanScreen(
        onQrScanned = {},
        onSkipClick = {}
    )
}
