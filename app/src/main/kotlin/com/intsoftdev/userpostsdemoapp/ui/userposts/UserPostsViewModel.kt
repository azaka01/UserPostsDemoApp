package com.intsoftdev.userpostsdemoapp.ui.userposts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intsoftdev.domain.PostsRepository
import com.intsoftdev.domain.PostsResults
import com.intsoftdev.domain.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class UserPostsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private var postsRepository: PostsRepository) : ViewModel() {

    val postsLiveData = MutableLiveData<PostsResults>()

    fun getUserPosts() {
        Timber.d("getUserPosts enter")
        viewModelScope.launch(dispatcher) {
            postsRepository.getUserPosts()
                .catch { throwable ->
                    Timber.e(throwable)
                    postsLiveData.postValue(ResultState.Failure(throwable))
                }.collect { postsResult ->
                    when (postsResult) {
                        is ResultState.Success -> {
                            Timber.d("ResultState.Success")
                            postsLiveData.postValue(postsResult)
                        }
                        is ResultState.Failure -> {
                            Timber.d("ResultState.Failure ${postsResult.error!!}")
                            postsLiveData.postValue(ResultState.Failure(postsResult.error))
                        }
                    }
                }
        }
    }
}