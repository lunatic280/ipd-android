package com.example.ipdapplication.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.community.CommunityUiState
import com.example.ipdapplication.community.CommunityViewModel
import com.example.ipdapplication.community.dto.BlogDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel = hiltViewModel(),
    onWriteClick: () -> Unit,
    onPostClick: (Long) -> Unit = {}
) {
    val state = viewModel.uiState
    CommunityScreenContent(
        state = state,
        onWriteClick = onWriteClick,
        onPostClick = onPostClick,
        onToggleLike = { viewModel.toggleBlogLike(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreenContent(
    state: CommunityUiState,
    onWriteClick: () -> Unit,
    onPostClick: (Long) -> Unit,
    onToggleLike: (BlogDto) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "커뮤니티",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onWriteClick) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "글 쓰기"
                )
            }
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
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                state.errorMessage?.let { msg ->
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }
                    }

                    state.blogs.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "아직 작성된 글이 없습니다.\n오른쪽 아래 버튼으로 첫 글을 작성해 보세요.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(state.blogs) { blog ->
                                BlogListItem(
                                    blog = blog,
                                    commentCount = state.comments.count { it.blogId == blog.id },
                                    onClick = { blog.id?.let(onPostClick) },
                                    onLikeClick = { onToggleLike(blog) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityScreenPreview() {
    var dummy by remember {
        mutableStateOf(
            CommunityUiState(
                blogs = listOf(
                    BlogDto(id = 1, title = "첫 번째 글", content = "테스트 본문", star = 12),
                    BlogDto(id = 2, title = "두 번째 글", content = "내용 미리보기 두 줄까지 노출됩니다.", star = 3)
                ),
                comments = emptyList(),
                isLoading = false
            )
        )
    }
    CommunityScreenContent(
        state = dummy,
        onWriteClick = {},
        onPostClick = {},
        onToggleLike = { blog ->
            val updated = dummy.blogs.map {
                if (it.id == blog.id) it.copy(star = it.star + 1) else it
            }
            dummy = dummy.copy(blogs = updated)
        }
    )
}
