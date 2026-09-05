package com.example.ipdapplication.qr.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

// QrScannerView.kt
@Composable
fun QrScannerView(
    onQrScanned: (String) -> Unit
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

    if (hasCameraPermission) {
        QrCameraPreview(
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
                Text("카메라 권한이 필요합니다")
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("권한 요청")
                }
            }
        }
    }
}

@Composable
private fun QrCameraPreview(
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
                            processImage(imageProxy, barcodeScanner) { qrData ->
                                hasScanned = true
                                onQrScanned(qrData)
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

        // 스캔 가이드 오버레이
        ScanOverlay()
    }
}

@Suppress("UnsafeOptInUsageError")
private fun processImage(
    imageProxy: ImageProxy,
    scanner: BarcodeScanner,
    onQrDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: run {
        imageProxy.close()
        return
    }

    val inputImage = InputImage.fromMediaImage(
        mediaImage,
        imageProxy.imageInfo.rotationDegrees
    )

    scanner.process(inputImage)
        .addOnSuccessListener { barcodes ->
            barcodes.firstOrNull()?.rawValue?.let { qrData ->
                onQrDetected(qrData)
            }
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}

@Composable
private fun ScanOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val scanBoxSize = 250.dp.toPx()
        val left = (size.width - scanBoxSize) / 2
        val top = (size.height - scanBoxSize) / 2

        // 반투명 배경
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            size = size
        )

        // 스캔 영역 (투명하게)
        drawRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(scanBoxSize, scanBoxSize),
            blendMode = BlendMode.Clear
        )

        // 모서리 라인
        val cornerLength = 30.dp.toPx()
        val strokeWidth = 4.dp.toPx()
        val cornerColor = Color.White

        // 좌상단
        drawLine(cornerColor, Offset(left, top), Offset(left + cornerLength, top), strokeWidth)
        drawLine(cornerColor, Offset(left, top), Offset(left, top + cornerLength), strokeWidth)

        // 우상단
        drawLine(cornerColor, Offset(left + scanBoxSize, top), Offset(left + scanBoxSize - cornerLength, top), strokeWidth)
        drawLine(cornerColor, Offset(left + scanBoxSize, top), Offset(left + scanBoxSize, top + cornerLength), strokeWidth)

        // 좌하단
        drawLine(cornerColor, Offset(left, top + scanBoxSize), Offset(left + cornerLength, top + scanBoxSize), strokeWidth)
        drawLine(cornerColor, Offset(left, top + scanBoxSize), Offset(left, top + scanBoxSize - cornerLength), strokeWidth)

        // 우하단
        drawLine(cornerColor, Offset(left + scanBoxSize, top + scanBoxSize), Offset(left + scanBoxSize - cornerLength, top + scanBoxSize), strokeWidth)
        drawLine(cornerColor, Offset(left + scanBoxSize, top + scanBoxSize), Offset(left + scanBoxSize, top + scanBoxSize - cornerLength), strokeWidth)
    }
}
