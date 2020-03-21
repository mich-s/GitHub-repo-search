package com.michs.github_repo_search.di

import com.michs.github_repo_search.network.HeaderInterceptor
import com.michs.github_repo_search.network.RepoService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule{

    @Singleton
    @Provides
    fun provideRepoService(okHttpClient: OkHttpClient) =
        provideService(okHttpClient, RepoService::class.java)

    private fun <T> provideService(okHttpClient: OkHttpClient, clazz: Class<T>): T =
        createRetrofit(okHttpClient).create(clazz)

    @Provides
    fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.HEADERS
            })
            .build()
    }

    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RepoService.endpoint)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    }

}
