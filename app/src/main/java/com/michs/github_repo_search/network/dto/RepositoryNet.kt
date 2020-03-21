package com.michs.github_repo_search.network.dto

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
    val language: String?
)