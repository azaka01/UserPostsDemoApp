package com.intsoftdev.userpostsdemoapp.ui.userposts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
class UserPostsViewModelTest {

    private val postsRepository: PostsRepository = mock()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var userPostsViewModel: UserPostsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userPostsViewModel = UserPostsViewModel(testDispatcher, postsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given repository when api returns posts then handled`() = runBlockingTest {
        // given
        val successResult = ResultState.Success(
            getTestList()
        )
        whenever(postsRepository.getUserPosts()).thenReturn(flowOf(successResult))

        // when
        userPostsViewModel.getUserPosts()

        // then
        assertEquals(successResult, userPostsViewModel.postsLiveData.value)
    }

    @Test
    fun `given repository when api returns error then error handled`() = runBlockingTest {
        // given
        val throwable = Throwable("Runtime exception")
        val error = ResultState.Failure(throwable)

        whenever(postsRepository.getUserPosts()).thenReturn(flowOf(error))

        // when
        userPostsViewModel.getUserPosts()

        // then
        assertEquals(error, userPostsViewModel.postsLiveData.value)
    }

    private fun getTestList() = listOf(post1, post2)

    companion object {
        private val post1 = PostModel(
            userId = "1",
            id = "1",
            title = "Some title 1",
            body = "body 1"
        )

        private val post2 = PostModel(
            userId = "2",
            id = "2",
            title = "Some title 2",
            body = "body 2"
        )
    }
}