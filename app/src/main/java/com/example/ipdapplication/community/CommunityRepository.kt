// app/src/main/java/com/example/ipdapplication/community/CommunityRepository.kt
package com.example.ipdapplication.community

import com.example.ipdapplication.community.Api.BlogApi
import com.example.ipdapplication.community.Api.BlogCommentApi
import com.example.ipdapplication.community.dto.BlogCommentLikeApi
import com.example.ipdapplication.community.Api.BlogLikeApi
import com.example.ipdapplication.community.dto.BlogCommentDto
import com.example.ipdapplication.community.dto.BlogDto
import com.example.ipdapplication.community.dto.BlogLikeDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepository @Inject constructor(
    private val blogApi: BlogApi,
    private val blogCommentApi: BlogCommentApi,
    private val blogLikeApi: BlogLikeApi,
    private val blogCommentLikeApi: BlogCommentLikeApi,
) {

    suspend fun getAllBlogs(): List<BlogDto> {
        return blogApi.getAllBlogs()
    }

    suspend fun createBlog(title: String, content: String): BlogDto {
        val dto = BlogDto(
            id = null,
            title = title,
            content = content,
            star = 0
        )
        return blogApi.createBlog(dto)
    }

    suspend fun updateBlog(id: Long, title: String, content: String): BlogDto {
        val dto = BlogDto(
            id = id,
            title = title,
            content = content,
            star = 0
        )
        return blogApi.updateBlog(id, dto)
    }

    suspend fun deleteBlog(id: Long) {
        blogApi.deleteBlog(id)
    }

    suspend fun getAllComments(): List<BlogCommentDto> =
        blogCommentApi.getAllComments()

    suspend fun createBlogComment(blogId: Long, content: String): BlogCommentDto {
        val dto = BlogCommentDto(
            id = null,
            blogId = blogId,
            content = content,
            star = 0
        )
        return blogCommentApi.createComment(dto)
    }

    suspend fun updateComment(commentId: Long, blogId: Long, content: String): BlogCommentDto {
        val dto = BlogCommentDto(
            id = commentId,
            blogId = blogId,
            content = content,
            star = 0
        )
        return blogCommentApi.updateComment(commentId, dto)
    }

    suspend fun deleteComment(commentId: Long) {
        blogCommentApi.deleteComment(commentId)
    }

    // ---------- 좋아요 API ----------

    suspend fun toggleBlogLike(blogId: Long): BlogLikeDto =
        blogLikeApi.toggleBlogLike(blogId)

    suspend fun toggleCommentLike(commentId: Long): BlogLikeDto =
        blogCommentLikeApi.toggleCommentLike(commentId)
}
