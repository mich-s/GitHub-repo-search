package com.michs.github_repo_search.network

import com.michs.github_repo_search.network.dto.RepositoryNet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepoService {

    companion object{
        const val endpoint = "https://api.github.com/"
    }

    @GET("search/repositories")
    suspend fun searchRepositories(@Query("q") query: String? = null): Response<ResultsResponse<RepositoryNet>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryInfo(@Path("owner") owner: String, @Path("repo") repo: String): Response<RepositoryNet>
}