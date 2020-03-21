package com.michs.github_repo_search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.repository.GitHubReposRepository
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var repository: GitHubReposRepository

    private val viewModel: MainViewModel by viewModels{ MainViewModelFactory(repository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.repos.observe(this, Observer { result ->
            when(result.status){
                Result.Status.SUCCESS -> {
                    Timber.d(result.data.toString())
                }
                Result.Status.ERROR -> {
                    Timber.d(result.message)
                }
                Result.Status.LOADING -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
