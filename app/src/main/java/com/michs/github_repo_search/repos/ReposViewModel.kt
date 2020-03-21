package com.michs.github_repo_search.repos

import androidx.lifecycle.ViewModel
import com.michs.github_repo_search.repository.GitHubReposRepository
import javax.inject.Inject

class ReposViewModel @Inject constructor(repository: GitHubReposRepository): ViewModel(){

    val repos = repository.repos
}