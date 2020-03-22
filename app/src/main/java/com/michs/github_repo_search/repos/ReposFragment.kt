package com.michs.github_repo_search.repos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.michs.github_repo_search.App
import com.michs.github_repo_search.MainActivity
import com.michs.github_repo_search.databinding.FragmentReposBinding
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.repository.GitHubReposRepository
import timber.log.Timber
import javax.inject.Inject

class ReposFragment: Fragment(){

    @Inject
    lateinit var repository: GitHubReposRepository

    private val viewModel: ReposViewModel by viewModels {ReposViewModelFactory(repository) }

    override fun onAttach(context: Context) {
        (activity!!.application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentReposBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        (activity as MainActivity).setSupportActionBar(binding.toolbar)

        binding.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                when(verticalOffset){
                    0 -> binding.searchCardView.isVisible = true
                    else -> binding.searchCardView.isVisible = false
                }
            }
        })


        viewModel.repos.observe(this, Observer { result ->
            when(result.status){
                Result.Status.SUCCESS -> {
                    Timber.d(result.data.toString())
                }
                Result.Status.ERROR -> {
                    Timber.d(result.message)
                }
                Result.Status.LOADING -> {
                    Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }
}