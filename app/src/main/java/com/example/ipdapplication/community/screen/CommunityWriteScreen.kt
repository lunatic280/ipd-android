package com.example.ipdapplication.community.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.community.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityWriteScreen(
    viewModel: CommunityViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val title = viewModel.blogTitleInput
    val content = viewModel.blogContentInput
    val state = viewModel.uiState

    CommunityWriteScreenContent(
        title = title,
        content = content,
        errorMessage = state.errorMessage,
        onTitleChange = viewModel::onBlogTitleChange,
        onContentChange = viewModel::onBlogContentChange,
        onSubmit = {
            viewModel.createBlog()
            onBack()
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityWriteScreenContent(
    title: String,
    content: String,
    errorMessage: String?,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("글 쓰기") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                errorMessage?.let { msg ->
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("제목") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = onContentChange,
                    label = { Text("내용") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onSubmit,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.End)
                ) {
                    Text("등록")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityWriteScreenPreview() {
    var title by remember { mutableStateOf("미리보기 제목") }
    var content by remember { mutableStateOf("미리보기 내용입니다.\n두 줄째") }
    CommunityWriteScreenContent(
        title = title,
        content = content,
        errorMessage = null,
        onTitleChange = { title = it },
        onContentChange = { content = it },
        onSubmit = {},
        onBack = {}
    )
}
