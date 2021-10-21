package com.intsoftdev.data.repository

import com.intsoftdev.data.cache.PostsCache
import com.intsoftdev.data.cache.toPostEntity
import com.intsoftdev.data.cache.toPostModel
import com.intsoftdev.data.network.PostsProxyService
import com.intsoftdev.domain.CommentsResults
import com.intsoftdev.domain.PostsRepository
import com.intsoftdev.domain.PostsResults
import com.intsoftdev.domain.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class PostsRepositoryImpl(
    private val postsProxyService: PostsProxyService,
    private val postsCache: PostsCache,
    private val requestDispatcher: CoroutineDispatcher
) : PostsRepository {

    private val MAX_RESULTS = 10

    override fun getUserPosts(): Flow<PostsResults> =
        flow<PostsResults> {
            val posts = postsProxyService.getPosts(maxResults = MAX_RESULTS)
            posts.map {
                it.toPostEntity()
            }.also {
                postsCache.insertPosts(it)
            }
            emit(ResultState.Success(posts))
        }.catch { throwable ->
            if (postsCache.isEmpty()) {
                emit(ResultState.Failure(throwable))
            } else {
                val cachedPosts = postsCache.getPosts().map {
                    it.toPostModel()
                }
                emit(ResultState.Success(cachedPosts))
            }
        }.flowOn(requestDispatcher)

    override fun getPostComments(postId: String): Flow<CommentsResults> =
        flow<CommentsResults> {
            emit(ResultState.Success(postsProxyService.getComments(postId = postId)))
        }.catch { throwable ->
            emit(ResultState.Failure(throwable))
        }.flowOn(requestDispatcher)
}