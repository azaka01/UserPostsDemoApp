package com.intsoftdev.userpostsdemoapp.ui.postdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.intsoftdev.domain.PostComments
import com.intsoftdev.domain.PostModel
import com.intsoftdev.domain.PostsRepository
import com.intsoftdev.domain.ResultState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PostDetailsViewModelTest {

    private val postsRepository: PostsRepository = mock()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var postDetailsViewModel : PostDetailsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        postDetailsViewModel = PostDetailsViewModel(testDispatcher, postsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given repository when api returns comments then handled`() = runBlockingTest {
        // given
        val postId = "1"
        val successResult = ResultState.Success(
            getTestList()
        )
        whenever(postsRepository.getPostComments(postId)).thenReturn(flowOf(successResult))

        // when
        postDetailsViewModel.getPostComments(postId)

        // then
        assertEquals(successResult, postDetailsViewModel.commentsLiveData.value)
    }

    @Test
    fun `given repository when api returns error then error handled`() =  runBlockingTest {
        // given
        val postId = "1"
        val throwable = Throwable("Runtime exception")
        val error = ResultState.Failure(throwable)

        whenever(postsRepository.getPostComments(postId)).thenReturn(flowOf(error))

        postDetailsViewModel.getPostComments(postId)

        // then
        assertEquals(error, postDetailsViewModel.commentsLiveData.value)
    }

    private fun getTestList() = listOf(comments1, comments2)

    companion object {
        private val comments1 = PostComments(
            postId = "1",
            id = "1",
            name = "Some title 1",
            email = "email1@mail.com",
            body = "body 1"
        )

        private val comments2 = PostComments(
            postId = "2",
            id = "2",
            name = "Some title 2",
            email = "email2@mail.com",
            body = "body 2"
        )
    }
}