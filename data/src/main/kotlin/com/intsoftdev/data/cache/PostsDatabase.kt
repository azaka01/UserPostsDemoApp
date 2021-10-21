package com.intsoftdev.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class], version = 1)
internal abstract class PostsDatabase : RoomDatabase() {
    abstract fun cachePostsDao(): CachedPostsDao
}