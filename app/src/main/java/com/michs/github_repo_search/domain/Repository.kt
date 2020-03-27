package com.michs.github_repo_search.domain

data class Repository(
    val name: String,
    val fullName: String,
    val owner: Owner,
    val htmlUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val language: String?,
    val description: String?,
    val stargazersCount: Int?,
    val forksCount: Int?
)