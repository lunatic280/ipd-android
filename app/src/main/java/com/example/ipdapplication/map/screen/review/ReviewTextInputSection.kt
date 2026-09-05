package com.example.ipdapplication.map.screen.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 리뷰 텍스트 입력 섹션
 */
@Composable
fun ReviewTextInputSection(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = LabelGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = GrayBorder,
                focusedBorderColor = MintGreen
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewTextInputSectionEmptyPreview() {
    ReviewTextInputSection(
        label = "지쿠 플레이스는",
        value = "",
        placeholder = "유명한 맛집이라길래 궁금했는데 진짜 맛있어용",
        onValueChange = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ReviewTextInputSectionFilledPreview() {
    ReviewTextInputSection(
        label = "지쿠 플레이스는",
        value = "유명한 맛집이라길래 궁금했는데 진짜 맛있어용",
        placeholder = "",
        onValueChange = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ReviewTextInputSectionBssPreview() {
    ReviewTextInputSection(
        label = "지쿠 BSS는",
        value = "",
        placeholder = "",
        onValueChange = {}
    )
}
