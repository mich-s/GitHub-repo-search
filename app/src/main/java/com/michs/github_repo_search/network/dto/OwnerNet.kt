package com.michs.github_repo_search.network.dto

import com.squareup.moshi.Json

data class OwnerNet(
    val login: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    val url: String
)