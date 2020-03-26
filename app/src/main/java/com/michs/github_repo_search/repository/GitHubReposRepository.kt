package com.michs.github_repo_search.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.michs.github_repo_search.domain.Repository
import com.michs.github_repo_search.network.RemoteDataSource
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.network.ResultsResponse
import com.michs.github_repo_search.network.dto.RepositoryNet
import com.michs.github_repo_search.utils.notifyObserver
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubReposRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    val repositories: LiveData<Result<ResultsResponse<RepositoryNet>>>
        get() = _repositories
    private val _repositories = MutableLiveData<Result<ResultsResponse<RepositoryNet>>>()

    fun searchRepositories(searchText: String) {
        scope.launch {
            val repos = remoteDataSource.getRepos(searchText)
            _repositories.notifyObserver(repos)
        }
    }

    fun fetchRepositoryDetails(owner: String, repo: String): LiveData<Result<RepositoryNet>> =
        liveData(Dispatchers.IO){
            val repository = remoteDataSource.getRepoDetail(owner, repo)
            emit(repository)
        }

}