package com.michs.github_repo_search.di

import android.content.Context
import com.michs.github_repo_search.MainActivity
import com.michs.github_repo_search.repoDetails.RepoDetailsFragment
import com.michs.github_repo_search.repos.ReposFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(reposFragment: ReposFragment)
    fun inject(repoDetailsFragment: RepoDetailsFragment)
}