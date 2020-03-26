package com.michs.github_repo_search.repoDetails

import androidx.lifecycle.ViewModel
import com.michs.github_repo_search.repository.GitHubReposRepository
import javax.inject.Inject

class RepoDetailsViewModel @Inject constructor(repository: GitHubReposRepository, val fullName: String): ViewModel(){

    var split = fullName.split("/")

    private val owner = split[0]
    private val repo = split[1]

    var repoDetail = repository.fetchRepositoryDetails(owner, repo)
}