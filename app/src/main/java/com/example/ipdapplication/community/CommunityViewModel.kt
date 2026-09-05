// app/src/main/java/com/example/ipdapplication/community/CommunityViewModel.kt
package com.example.ipdapplication.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.community.dto.BlogCommentDto
import com.example.ipdapplication.community.dto.BlogDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class CommunityUiState(
    val isLoading: Boolean = false,
    val blogs: List<BlogDto> = emptyList(),
    val comments: List<BlogCommentDto> = emptyList(),
    val selectedBlogId: Long? = null,
    val errorMessage: String? = null,
)

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository
) : ViewModel() {

    var uiState by mutableStateOf(CommunityUiState(isLoading = true))
        private set

    var blogTitleInput by mutableStateOf("")
        private set

    var blogContentInput by mutableStateOf("")
        private set

    var commentInput by mutableStateOf("")
        private set

    init {
        reloadAll()
    }

    fun reloadAll() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            runCatching {
                val blogs = repository.getAllBlogs()
                val comments = repository.getAllComments()
                blogs to comments
            }.onSuccess { (blogs, comments) ->
                uiState = uiState.copy(
                    isLoading = false,
                    blogs = blogs,
                    comments = comments,
                    selectedBlogId = uiState.selectedBlogId ?: blogs.firstOrNull()?.id
                )
            }.onFailure { e ->
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "데이터를 불러오는데 실패했습니다."
                )
            }
        }
    }

    fun onBlogTitleChange(newTitle: String) {
        blogTitleInput = newTitle
    }

    fun onBlogContentChange(newContent: String) {
        blogContentInput = newContent
    }

    fun onCommentChange(newComment: String) {
        commentInput = newComment
    }

    fun selectBlog(blogId: Long) {
        uiState = uiState.copy(selectedBlogId = blogId)
    }

    fun createBlog() {
        val title = blogTitleInput.trim()
        val content = blogContentInput.trim()

        if (title.isBlank() || content.isBlank()) {
            uiState = uiState.copy(errorMessage = "제목과 내용을 모두 입력해주세요.")
            return
        }

        viewModelScope.launch {
            runCatching {
                repository.createBlog(title, content)
            }.onSuccess { newBlog ->
                uiState = uiState.copy(
                    blogs = listOf(newBlog) + uiState.blogs,
                    selectedBlogId = newBlog.id
                )
                blogTitleInput = ""
                blogContentInput = ""
            }.onFailure { e ->
                uiState = uiState.copy(
                    errorMessage = "블로그 생성에 실패했습니다."
                )
            }
        }
    }

    fun createComment() {
        val blogId = uiState.selectedBlogId ?: run {
            uiState = uiState.copy(errorMessage = "먼저 글을 선택해주세요.")
            return
        }
        val content = commentInput.trim()
        if (content.isBlank()) {
            uiState = uiState.copy(errorMessage = "댓글 내용을 입력해주세요.")
            return
        }

        viewModelScope.launch {
            runCatching {
                repository.createBlogComment(blogId, content)
            }.onSuccess { newComment ->
                uiState = uiState.copy(
                    comments = uiState.comments + newComment
                )
                commentInput = ""
            }.onFailure { e ->
                uiState = uiState.copy(
                    errorMessage = "댓글 작성에 실패했습니다."
                )
            }
        }
    }

    /**
     * 댓글 좋아요 토글 (한 계정당 한 번만 좋아요)
     */
    fun toggleCommentLike(comment: BlogCommentDto) {
        val commentId = comment.id ?: return
        viewModelScope.launch {
            runCatching {
                repository.toggleCommentLike(commentId)
            }.onSuccess { likeDto ->
                uiState = uiState.copy(
                    comments = uiState.comments.map {
                        if (it.id == commentId) {
                            it.copy(star = likeDto.likeCount.toInt())
                        } else {
                            it
                        }
                    }
                )
            }.onFailure { e ->
                uiState = uiState.copy(
                    errorMessage = "댓글 좋아요 처리에 실패했습니다."
                )
            }
        }
    }

    /**
     * 블로그(게시글) 좋아요 토글
     */
    fun toggleBlogLike(blog: BlogDto) {
        val blogId = blog.id ?: return
        viewModelScope.launch {
            runCatching {
                repository.toggleBlogLike(blogId)
            }.onSuccess { likeDto ->
                uiState = uiState.copy(
                    blogs = uiState.blogs.map {
                        if (it.id == blogId) {
                            it.copy(star = likeDto.likeCount.toInt())
                        } else {
                            it
                        }
                    }
                )
            }.onFailure { e ->
                uiState = uiState.copy(
                    errorMessage = "게시글 좋아요 처리에 실패했습니다."
                )
            }
        }
    }
}
