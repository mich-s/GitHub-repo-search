package com.michs.github_repo_search.repoDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michs.github_repo_search.repository.GitHubReposRepository
import javax.inject.Inject

class RepoDetailsViewModelFactory @Inject constructor(val repository: GitHubReposRepository, private val fullName: String): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RepoDetailsViewModel::class.java)){
            return RepoDetailsViewModel(repository, fullName) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}