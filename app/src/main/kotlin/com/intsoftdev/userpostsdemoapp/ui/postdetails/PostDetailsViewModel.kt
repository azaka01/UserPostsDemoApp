package com.intsoftdev.userpostsdemoapp.ui.postdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intsoftdev.domain.CommentsResults
import com.intsoftdev.domain.PostsRepository
import com.intsoftdev.domain.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class PostDetailsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private var postsRepository: PostsRepository) : ViewModel()  {

    val commentsLiveData = MutableLiveData<CommentsResults>()

    fun getPostComments(postId: String) {
        Timber.d("getPostComments for post $postId")
        viewModelScope.launch(dispatcher) {
            postsRepository.getPostComments(postId)
                .catch { throwable ->
                    Timber.e(throwable)
                    commentsLiveData.postValue(ResultState.Failure(throwable))
                }.collect { postsResult ->
                    when (postsResult) {
                        is ResultState.Success -> {
                            commentsLiveData.postValue(postsResult)
                        }
                        is ResultState.Failure -> {
                            Timber.e(postsResult.error)
                            commentsLiveData.postValue(ResultState.Failure(postsResult.error))
                        }
                    }
                }
        }
    }
}