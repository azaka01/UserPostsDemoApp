package com.intsoftdev.data.repository

import app.cash.turbine.test
import com.intsoftdev.data.cache.CacheState
import com.intsoftdev.data.cache.PostEntity
import com.intsoftdev.data.cache.PostsCache
import com.intsoftdev.data.network.PostsProxyService
import com.intsoftdev.domain.PostModel
import com.intsoftdev.domain.ResultState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class PostsRepositoryImplTest {

    private val postsService = mock<PostsProxyService>()
    private val postsCache = mock<PostsCache>()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var postsRepository: PostsRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        postsRepository = PostsRepositoryImpl(postsService, postsCache, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `given stale cache when posts service queried posts are returned`() =
        runBlockingTest(testDispatcher) {
            whenever(postsCache.getCacheState()).thenReturn(CacheState.Stale)
            whenever(postsService.getPosts(10)).thenReturn(getTestList())

            postsRepository.getUserPosts().test {
                assertEquals(ResultState.Success(getTestList()), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `given empty cache when posts service queried posts are returned`() =
        runBlockingTest(testDispatcher) {
            whenever(postsCache.getCacheState()).thenReturn(CacheState.Empty)
            whenever(postsService.getPosts(10)).thenReturn(getTestList())

            postsRepository.getUserPosts().test {
                assertEquals(ResultState.Success(getTestList()), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `given usable cache when posts cache queried posts are returned`() =
        runBlockingTest(testDispatcher) {
            whenever(postsCache.getCacheState()).thenReturn(CacheState.Usable)
            whenever(postsCache.getPosts()).thenReturn(getCacheList())

            postsRepository.getUserPosts().test {
                assertEquals(ResultState.Success(getTestList()), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `given empty cache when posts service exception failure is returned`() =
        runBlockingTest(testDispatcher) {
            val exception = RuntimeException("oops")
            whenever(postsCache.getCacheState()).thenReturn(CacheState.Empty)
            whenever(postsService.getPosts(10)).thenThrow(exception)

            postsRepository.getUserPosts().test {
                assertEquals(ResultState.Failure(exception), awaitItem())
                awaitComplete()
            }
        }

    private fun getTestList() = listOf(post1)
    private fun getCacheList() = listOf(cache1)

    companion object {
        private val post1 = PostModel(
            userId = "1",
            id = "1",
            title = "Some title 1",
            body = "body 1"
        )
        private val cache1 = PostEntity(
            userId = "1",
            postId = "1",
            title = "Some title 1",
            body = "body 1"
        )
    }
}