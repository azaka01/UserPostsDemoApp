package com.intsoftdev.userpostsdemoapp

import android.app.Application
import com.intsoftdev.data.di.dataModule
import com.intsoftdev.userpostsdemoapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class UserPostsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            // declare used Android context
            androidContext(this@UserPostsApplication)
            modules(listOf(viewModelModule, dataModule))
        }
    }
}