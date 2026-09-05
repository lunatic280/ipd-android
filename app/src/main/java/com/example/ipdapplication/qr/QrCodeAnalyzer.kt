// QrCodeAnalyzer.kt
package com.example.ipdapplication.qr.camera

import android.annotation.SuppressLint

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

private const val TAG = "QrCodeAnalyzer"

class QrCodeAnalyzer(
    private val onQrScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val qr = barcodes.firstOrNull { !it.rawValue.isNullOrBlank() }?.rawValue

                if (qr != null) {
                    // 매번 인식될 때마다 콜백 호출
                    onQrScanned(qr)
                }
            }
            .addOnFailureListener { e ->
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
