package com.intsoftdev.userpostsdemoapp.di

import com.intsoftdev.userpostsdemoapp.ui.postdetails.PostDetailsViewModel
import com.intsoftdev.userpostsdemoapp.ui.userposts.UserPostsViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        UserPostsViewModel(get(), get())
    }
    viewModel {
        PostDetailsViewModel(get(), get())
    }
    factory {
        Dispatchers.Default
    }
}
