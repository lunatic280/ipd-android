// app/src/main/java/com/example/ipdapplication/qr/screen/QrCameraView.kt
package com.example.ipdapplication.qr.screen

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ipdapplication.qr.camera.QrCodeAnalyzer
import java.util.concurrent.Executors

@Composable
fun QrCameraView(
    modifier: Modifier = Modifier,
    onQrScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var hasScanned by remember { mutableStateOf(false) }

    val analyzer = remember {
        QrCodeAnalyzer { qrText ->
            if (!hasScanned) {
                hasScanned = true
                onQrScanned(qrText)
            }
        }
    }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it.setAnalyzer(cameraExecutor, analyzer) }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )
    }

    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // 스캔 가이드 오버레이
        ScanOverlay()
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
