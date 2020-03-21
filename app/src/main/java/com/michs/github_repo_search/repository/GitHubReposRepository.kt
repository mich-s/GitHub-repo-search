package com.michs.github_repo_search.repository

import androidx.lifecycle.liveData
import com.michs.github_repo_search.network.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GitHubReposRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    val repos = liveData(Dispatchers.IO) {
        val data = remoteDataSource.getRepos()
        emit(data)
    }


}