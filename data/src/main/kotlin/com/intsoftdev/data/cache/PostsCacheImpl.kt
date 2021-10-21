package com.intsoftdev.data.cache

import com.intsoftdev.data.preferences.PreferencesHelper
import kotlinx.datetime.Clock

internal class PostsCacheImpl(
    private val postsDatabase: PostsDatabase,
    private val preferencesHelper: PreferencesHelper,
    private val clock: Clock
) : PostsCache {

    companion object {
        private const val EXPIRATION_TIME_MS = (60 * 1000).toLong() // 60 seconds
    }

    override suspend fun getPosts(): List<PostEntity> = postsDatabase.cachePostsDao().getPosts()

    override suspend fun insertPosts(posts: List<PostEntity>) {
        postsDatabase.cachePostsDao().insertPostsList(posts)
        setLastUpdateTime()
    }

    override fun getCacheState(): CacheState {
        if (isEmpty()) return CacheState.Empty

        // has cache time expiry reached
        return if (doUpdate())
            CacheState.Stale

        // else cache is ok
        else CacheState.Usable
    }

    private fun isEmpty(): Boolean = postsDatabase.cachePostsDao().getPosts().isNullOrEmpty()

    private fun setLastUpdateTime() {
        val currentTimeMS = clock.now().toEpochMilliseconds()
        preferencesHelper.lastUpdateTime = currentTimeMS
    }

    /**
     * Get in millis, the last time the data was accessed.
     */
    private fun getLastUpdateTimeMillis(): Long {
        return preferencesHelper.lastUpdateTime
    }

    /**
     * Checks whether the last update time exceeds the defined [EXPIRATION_TIME_MS] time.
     */
    private fun doUpdate(): Boolean {
        val currentTimeMS = clock.now().toEpochMilliseconds()
        val lastDownloadTimeMS = this.getLastUpdateTimeMillis()
        return currentTimeMS - lastDownloadTimeMS > EXPIRATION_TIME_MS
    }
}