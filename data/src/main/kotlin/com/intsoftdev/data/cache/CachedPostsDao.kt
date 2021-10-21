package com.intsoftdev.data.cache

import androidx.room.*

@Dao
internal interface CachedPostsDao {

    @Query("SELECT * from postsTable")
    fun getPosts(): List<PostEntity>

    @Query("DELETE from postsTable")
    fun clearPosts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(postEntities: List<PostEntity>)

    @Transaction
    fun insertPostsList(list: List<PostEntity>) {
        clearPosts()
        insertPosts(list)
    }
}