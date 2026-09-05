package com.example.ipdapplication.community.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ipdapplication.community.CommunityViewModel
import com.example.ipdapplication.community.dto.BlogCommentDto
import com.example.ipdapplication.community.dto.BlogDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailScreen(
    blogId: Long,
    viewModel: CommunityViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state = viewModel.uiState
    val commentInput = viewModel.commentInput

    LaunchedEffect(blogId) {
        viewModel.selectBlog(blogId)
    }

    val blog: BlogDto? = state.blogs.find { it.id == blogId }
    val commentsForBlog: List<BlogCommentDto> =
        state.comments.filter { it.blogId == blogId }

    BlogDetailScreenContent(
        blog = blog,
        comments = commentsForBlog,
        commentInput = commentInput,
        isLoading = state.isLoading && blog == null,
        errorMessage = state.errorMessage,
        onBack = onBack,
        onCommentChange = viewModel::onCommentChange,
        onAddComment = viewModel::createComment,
        onToggleBlogLike = { blog?.let(viewModel::toggleBlogLike) },
        onToggleCommentLike = viewModel::toggleCommentLike
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailScreenContent(
    blog: BlogDto?,
    comments: List<BlogCommentDto>,
    commentInput: String,
    isLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onCommentChange: (String) -> Unit,
    onAddComment: () -> Unit,
    onToggleBlogLike: () -> Unit,
    onToggleCommentLike: (BlogCommentDto) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = blog?.title ?: "게시글",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
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
            when {
                isLoading && blog == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                blog == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("게시글을 찾을 수 없습니다.")
                    }
                }

                else -> {
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

                        BlogDetailHeader(
                            blog = blog,
                            onLikeClick = onToggleBlogLike
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "댓글",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = commentInput,
                            onValueChange = onCommentChange,
                            label = { Text("댓글을 입력하세요") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 60.dp),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = onAddComment) {
                                Text("등록")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (comments.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("첫 댓글을 남겨보세요!")
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                items(comments) { comment ->
                                    CommentItem(
                                        comment = comment,
                                        onStarClick = { onToggleCommentLike(comment) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BlogDetailHeader(
    blog: BlogDto,
    onLikeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = blog.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = blog.content,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "좋아요 ${blog.star}",
                    style = MaterialTheme.typography.bodySmall
                )
                Button(onClick = onLikeClick) {
                    Text("추천")
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: BlogCommentDto,
    onStarClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "좋아요 ${comment.star}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onStarClick) {
                Text("추천")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlogDetailScreenPreview() {
    val dummyBlog = BlogDto(id = 1, title = "제목", content = "본문 미리보기입니다.", star = 12)
    val dummyComments = listOf(
        BlogCommentDto(id = 1, blogId = 1, content = "첫 댓글", star = 2),
        BlogCommentDto(id = 2, blogId = 1, content = "두 번째 댓글", star = 0)
    )
    var commentInput by remember { mutableStateOf("새 댓글 내용") }
    BlogDetailScreenContent(
        blog = dummyBlog,
        comments = dummyComments,
        commentInput = commentInput,
        isLoading = false,
        errorMessage = null,
        onBack = {},
        onCommentChange = { commentInput = it },
        onAddComment = {},
        onToggleBlogLike = {},
        onToggleCommentLike = {}
    )
}
