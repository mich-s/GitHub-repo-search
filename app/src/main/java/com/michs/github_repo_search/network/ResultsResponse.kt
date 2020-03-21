package com.michs.github_repo_search.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultsResponse<T>(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "incomplete_results") val incompleteResults: Boolean,
    val items: List<T>
)