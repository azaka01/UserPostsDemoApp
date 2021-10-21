package com.intsoftdev.domain

sealed class ResultState<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultState<T>()
    data class Failure(val error: Throwable?) : ResultState<Nothing>()
}