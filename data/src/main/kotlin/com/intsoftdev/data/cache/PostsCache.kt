package com.intsoftdev.data.cache

interface PostsCache {
    suspend fun getPosts(): List<PostEntity>
    suspend fun isEmpty(): Boolean
    suspend fun insertPosts(posts: List<PostEntity>)
}