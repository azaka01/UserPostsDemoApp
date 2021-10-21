package com.intsoftdev.domain

import kotlinx.coroutines.flow.Flow

typealias PostsResults = ResultState<List<PostModel>>
typealias CommentsResults = ResultState<List<PostComments>>

interface PostsRepository {
    fun getUserPosts(): Flow<PostsResults>
    fun getPostComments(postId: String): Flow<CommentsResults>
}