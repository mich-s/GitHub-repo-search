package com.michs.github_repo_search.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().header("Accept", "application/vnd.github.v3+json").build()
        return chain.proceed(request)
    }
}