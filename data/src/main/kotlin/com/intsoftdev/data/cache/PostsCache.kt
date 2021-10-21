package com.intsoftdev.data.cache

interface PostsCache {
    suspend fun getPosts(): List<PostEntity>
    suspend fun insertPosts(posts: List<PostEntity>)
    fun getCacheState(): CacheState
}