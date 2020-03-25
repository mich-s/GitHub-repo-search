package com.michs.github_repo_search.network

import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val repoService: RepoService){

    suspend fun getRepos(query: String?) = getResult { repoService.searchRepositories(query) }

    private suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null)
                    return Result.success(body)
            }
            return error("${response.code()}, ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        Timber.e(message)
        return Result.error(null, "Network call has failed: $message")
    }
}