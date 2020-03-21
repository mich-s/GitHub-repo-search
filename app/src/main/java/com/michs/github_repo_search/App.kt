package com.michs.github_repo_search

import android.app.Application
import com.michs.github_repo_search.di.DaggerAppComponent
import timber.log.Timber

class App: Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}