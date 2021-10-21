package com.intsoftdev.data.cache

import com.intsoftdev.data.preferences.PreferencesHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class ClockMock(var currentInstant: Instant) : Clock {
    override fun now(): Instant = currentInstant
}

@ExperimentalTime
class PostsCacheImplTest {

    private val postsDatabase = mock<PostsDatabase>()
    private val preferencesHelper = mock<PreferencesHelper>()
    private val mockDao = mock<CachedPostsDao>()
    private val clock = ClockMock(Clock.System.now())

    private lateinit var postsCache: PostsCacheImpl

    @Before
    fun setUp() {
        postsCache = PostsCacheImpl(postsDatabase, preferencesHelper, clock)
    }

    @Test
    fun `given empty cache correct cache state is returned`() {
        whenever(mockDao.getPosts()).thenReturn(emptyList())
        whenever(postsDatabase.cachePostsDao()).thenReturn(mockDao)

        assertEquals(CacheState.Empty, postsCache.getCacheState())
    }

    @Test
    fun `given usable cache correct cache state is returned`() {
        whenever(mockDao.getPosts()).thenReturn(listOf(cache1))
        whenever(postsDatabase.cachePostsDao()).thenReturn(mockDao)

        val currentTimeMS = Clock.System.now().toEpochMilliseconds()
        whenever(preferencesHelper.lastUpdateTime).thenReturn(currentTimeMS)

        assertEquals(CacheState.Usable, postsCache.getCacheState())
    }

    @Test
    fun `given stale cache correct cache state is returned`() {
        whenever(mockDao.getPosts()).thenReturn(listOf(cache1))
        whenever(postsDatabase.cachePostsDao()).thenReturn(mockDao)

        val currentTimeMS = Clock.System.now().toEpochMilliseconds()
        whenever(preferencesHelper.lastUpdateTime).thenReturn(currentTimeMS)

        clock.currentInstant += Duration.Companion.minutes(10)
        assertEquals(CacheState.Stale, postsCache.getCacheState())
    }


    companion object {
        private val cache1 = PostEntity(
            userId = "1",
            postId = "1",
            title = "Some title 1",
            body = "body 1"
        )
    }
}