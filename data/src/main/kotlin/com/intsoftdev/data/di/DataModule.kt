package com.intsoftdev.data.di

import androidx.room.Room
import com.intsoftdev.data.cache.PostsCache
import com.intsoftdev.data.cache.PostsCacheImpl
import com.intsoftdev.data.cache.PostsDatabase
import com.intsoftdev.data.network.PostsProxyService
import com.intsoftdev.data.repository.PostsRepositoryImpl
import com.intsoftdev.domain.PostsRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import org.koin.dsl.module

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
private const val DEFAULT_TIMEOUT = 30L

val dataModule = module {

    factory<PostsProxyService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PostsProxyService::class.java)
    }

    factory {
        OkHttpClient().newBuilder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            .build()
    }

    factory<PostsRepository> {
        PostsRepositoryImpl(
            postsProxyService = get(),
            postsCache = get(),
            requestDispatcher = Dispatchers.Default
        )
    }

    factory { get<PostsDatabase>().cachePostsDao() }

    single {
        Room.databaseBuilder(
            androidApplication(),
            PostsDatabase::class.java,
            "posts.db"
        ).build()
    }

    factory<PostsCache> { PostsCacheImpl(get()) }
}