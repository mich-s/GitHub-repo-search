package com.michs.github_repo_search.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OwnerNet(
    val login: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    val url: String
)