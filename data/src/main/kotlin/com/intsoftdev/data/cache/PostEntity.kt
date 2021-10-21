package com.intsoftdev.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.intsoftdev.domain.PostModel

@Entity(tableName = "postsTable")
data class PostEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "postId") val postId: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "body") val body: String
)

internal fun PostModel.toPostEntity() = PostEntity(
    postId = this.id,
    userId = this.userId,
    title = this.title,
    body = this.body
)

internal fun PostEntity.toPostModel() = PostModel(
    id = this.postId,
    userId = this.userId,
    title = this.title,
    body = this.body
)