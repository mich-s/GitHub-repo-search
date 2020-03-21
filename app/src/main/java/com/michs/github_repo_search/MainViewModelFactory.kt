package com.michs.github_repo_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michs.github_repo_search.repository.GitHubReposRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(val repository: GitHubReposRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}