package com.example.ipdapplication.map.screen.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SavePlaceBottomBar(
    onSave: (String, String?, String, String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var telNumber by remember { mutableStateOf("") }
    var placeType by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("\uc81c\ubaa9") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("\uc124\uba85(\uc120\ud0dd)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = telNumber,
                onValueChange = { telNumber = it },
                label = { Text("\uc804\ud654\ubc88\ud638") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = placeType,
                onValueChange = { placeType = it },
                label = { Text("\uc704\uce58 \ud0c0\uc785") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) { Text("\ucde8\uc18c") }
                Spacer(Modifier.width(8.dp))
                Button(
                    enabled = title.isNotBlank() && telNumber.isNotBlank() && placeType.isNotBlank(),
                    onClick = {
                        onSave(
                            title.trim(),
                            desc.ifBlank { null },
                            telNumber.trim(),
                            placeType.trim()
                        )
                    }
                ) {
                    Text("\uc800\uc7a5")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SavePlaceBottomBarPreview() {
    SavePlaceBottomBar(
        onSave = { _, _, _, _ -> },
        onCancel = {}
    )
}
