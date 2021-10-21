package com.intsoftdev.data.repository

import com.intsoftdev.data.cache.CacheState
import com.intsoftdev.data.cache.PostsCache
import com.intsoftdev.data.cache.toPostEntity
import com.intsoftdev.data.cache.toPostModel
import com.intsoftdev.data.network.PostsProxyService
import com.intsoftdev.domain.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

internal class PostsRepositoryImpl(
    private val postsProxyService: PostsProxyService,
    private val postsCache: PostsCache,
    private val requestDispatcher: CoroutineDispatcher
) : PostsRepository {

    private val MAX_RESULTS = 10

    override fun getUserPosts(): Flow<PostsResults> =
        flow<PostsResults> {
            when (val state = postsCache.getCacheState()) {
                is CacheState.Empty, CacheState.Stale -> {
                    Timber.d("cache state is $state")
                    emit(ResultState.Success(refreshPosts()))
                }
                is CacheState.Usable -> {
                    Timber.d("cache state is $state")
                    emit(
                        ResultState.Success(
                            postsCache.getPosts().map {
                                it.toPostModel()
                            }
                        )
                    )
                }
            }
        }.catch { throwable ->
            emit(ResultState.Failure(throwable))
        }.flowOn(requestDispatcher)

    private suspend fun refreshPosts(): List<PostModel> {
        val posts = postsProxyService.getPosts(maxResults = MAX_RESULTS)
        posts.map {
            it.toPostEntity()
        }.also {
            postsCache.insertPosts(it)
        }
        return posts
    }

    override fun getPostComments(postId: String): Flow<CommentsResults> =
        flow<CommentsResults> {
            // query the service for getUsers and extract the user name given the email from getComments
            emit(ResultState.Success(postsProxyService.getComments(postId = postId)))
        }.catch { throwable ->
            emit(ResultState.Failure(throwable))
        }.flowOn(requestDispatcher)
}