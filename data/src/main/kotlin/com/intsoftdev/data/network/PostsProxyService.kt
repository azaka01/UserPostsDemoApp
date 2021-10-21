package com.intsoftdev.data.network

import com.intsoftdev.domain.PostComments
import com.intsoftdev.domain.PostModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the abstract methods used for interacting with the https://jsonplaceholder.typicode.com/
 */
internal interface PostsProxyService {
    @GET("posts/")
    suspend fun getPosts(@Query("_limit") maxResults: Int): List<PostModel>

    @GET("comments/")
    suspend fun getComments(@Query("postId") postId: String): List<PostComments>
}