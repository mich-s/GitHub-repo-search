package com.michs.github_repo_search.repos

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.michs.github_repo_search.App
import com.michs.github_repo_search.MainActivity
import com.michs.github_repo_search.R
import com.michs.github_repo_search.databinding.FragmentReposBinding
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.network.dto.asDomainModel
import com.michs.github_repo_search.repository.GitHubReposRepository
import com.michs.github_repo_search.utils.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_repos.*
import kotlinx.coroutines.*
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

        val adapter = ReposAdapter(RepoClick {
            val direction = ReposFragmentDirections.actionReposFragmentToRepoDetailsFragment(it.fullName)
            findNavController().navigate(direction)
        })



        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.repositories.observe(this, Observer { result ->
            when(result?.status){
                Result.Status.SUCCESS -> {
                    binding.noResults.isVisible = false
//                    binding.loadingLayout.isVisible = false
                    Timber.d(result.data.toString())
                    result.data?.items?.let { adapter.submitList(it.asDomainModel()) }
                }
                Result.Status.ERROR -> {
                    binding.noResults.isVisible = false
//                    binding.loadingLayout.isVisible = false
                    Timber.d(result.message)
                    val areTooManyRequests = result.message!!.contains("403")
                    if (areTooManyRequests)
                        Toast.makeText(activity, "Too many requests", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }
}