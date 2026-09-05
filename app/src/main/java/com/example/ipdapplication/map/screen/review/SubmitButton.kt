package com.example.ipdapplication.map.screen.review

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 등록 버튼
 */
@Composable
fun SubmitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "등록"
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MintGreen
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SubmitButtonPreview() {
    SubmitButton(onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun SubmitButtonCustomTextPreview() {
    SubmitButton(
        onClick = {},
        text = "저장하기"
    )
}
