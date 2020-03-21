package com.michs.github_repo_search.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michs.github_repo_search.repository.GitHubReposRepository
import javax.inject.Inject

class ReposViewModelFactory @Inject constructor(val repository: GitHubReposRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReposViewModel::class.java)){
            return ReposViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}