package com.intsoftdev.data.cache

internal class PostsCacheImpl(private val postsDatabase: PostsDatabase) : PostsCache {

    override suspend fun getPosts(): List<PostEntity> = postsDatabase.cachePostsDao().getPosts()

    override suspend fun isEmpty(): Boolean = postsDatabase.cachePostsDao().getPosts().isNullOrEmpty()

    override suspend fun insertPosts(posts: List<PostEntity>) {
        postsDatabase.cachePostsDao().insertPostsList(posts)
    }
}