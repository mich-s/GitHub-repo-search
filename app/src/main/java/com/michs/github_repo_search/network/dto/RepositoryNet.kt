package com.michs.github_repo_search.network.dto

import com.michs.github_repo_search.domain.Repository
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryNet(
    val name: String,
    @Json(name = "full_name") val fullName: String,
    val owner: OwnerNet,
    @Json(name = "html_url") val htmlUrl: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    val language: String?,
    val description: String?,
    @Json(name = "stargazers_count") val stargazersCount: Int,
    @Json(name = "forks_count") val forksCount: Int,
    @Json(name = "watchers_count") val watchersCount: Int
)

fun List<RepositoryNet>.asDomainModel(): List<Repository> =
    map {
        Repository(
            name = it.name,
            fullName = it.fullName,
            owner = it.owner.asDomainObject(),
            htmlUrl = it.htmlUrl,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt,
            language = it.language,
            description = it.description,
            stargazersCount = it.stargazersCount,
            forksCount = it.forksCount,
            watchersCount = it.watchersCount
        )
    }

fun RepositoryNet.asDomainObject(): Repository =
    Repository(
        name = name,
        fullName = fullName,
        owner = owner.asDomainObject(),
        htmlUrl = htmlUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        language = language,
        description = description,
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        watchersCount = watchersCount
    )
