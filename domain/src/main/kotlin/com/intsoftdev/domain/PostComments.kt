package com.intsoftdev.domain

data class PostComments(
    val postId: String,
    val id: String,
    val name: String,
    val email: String,
    val body: String
)
