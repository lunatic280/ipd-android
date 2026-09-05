package com.example.ipdapplication.qr.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ipdapplication.qr.viewmodel.QrScanUiState
import com.example.ipdapplication.qr.viewmodel.QrScanViewModel

@Composable
fun QrControlScreen(
    viewModel: QrScanViewModel = hiltViewModel(),
    onBackToMain: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val issuedCoupon by viewModel.issuedCoupon.collectAsStateWithLifecycle()

    // 화면이 다시 보여질 때 스캔 상태로 리셋
    DisposableEffect(Unit) {
        viewModel.resetToScanning()
        onDispose { }
    }

    when (val state = uiState) {
        is QrScanUiState.Scanning -> {
            // QR 스캔 화면
            QrScanScreen(
                onQrScanned = { qrData ->
                    viewModel.onQrScanned(qrData)
                },
                onSkipClick = onBackToMain
            )
        }

        is QrScanUiState.FirstStepInProgress -> {
            // 첫 번째 단계: 배터리 분리 중 화면 (openBss 호출)
            BatterySwapInProgressScreen(
                slotNumber = state.slotNumber,
                onSwapComplete = {
                    viewModel.onFirstStepComplete()
                }
            )
        }

        is QrScanUiState.SecondStepInProgress -> {
            // 두 번째 단계: 배터리 장착 중 화면 (nextStep 호출)
            BatterySwapSecondStepScreen(
                slotNumber = state.slotNumber,
                onStepComplete = {
                    viewModel.onSecondStepComplete()
                }
            )
        }

        is QrScanUiState.SwapComplete -> {
            // 배터리 교체 완료 화면 (쿠폰 정보 포함)
            BatterySwapCompleteScreen(
                slotNumber = state.slotNumber,
                autoNavigateDelayMs = 5000,  // 쿠폰 애니메이션 볼 시간 확보
                issuedCoupon = issuedCoupon,
                onConfirmClick = onBackToMain
            )
        }

        is QrScanUiState.SwapInProgress -> {
            // 기존 호환성 - SwapInProgress 상태도 첫 번째 단계로 표시
            BatterySwapInProgressScreen(
                slotNumber = state.slotNumber,
                onSwapComplete = {
                    viewModel.onSwapComplete()
                }
            )
        }

        is QrScanUiState.Success -> {
            // 기존 호환성 - Success 상태도 완료 화면으로 표시
            BatterySwapCompleteScreen(
                slotNumber = "BSS : ${state.data.takeLast(5)}",
                autoNavigateDelayMs = 5000,
                issuedCoupon = issuedCoupon,
                onConfirmClick = onBackToMain
            )
        }
    }
}
