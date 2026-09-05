package com.example.ipdapplication.map.screen.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ipdapplication.map.dto.CommentDto
import com.example.ipdapplication.map.dto.PlaceLikeDto

@Composable
fun CommentItem(
    comment: CommentDto,
    like: PlaceLikeDto?,
    onToggleLike: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleLike) {
                Icon(
                    imageVector = if (like?.liked == true)
                        Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = "댓글 좋아요",
                    tint = if (like?.liked == true) Color(0xFFE91E63) else Color(0xFF9E9E9E)
                )
            }
            Text(
                text = (like?.likeCount ?: 0L).toString(),
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF4B4B4B)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentItemPreview() {
    CommentItem(
        comment = CommentDto(id = 1L, placeId = 1L, content = "맛있고 친절해요!"),
        like = PlaceLikeDto(liked = true, likeCount = 3),
        onToggleLike = {}
    )
}
