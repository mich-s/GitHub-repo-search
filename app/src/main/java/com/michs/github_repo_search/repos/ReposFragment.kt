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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michs.github_repo_search.App
import com.michs.github_repo_search.MainActivity
import com.michs.github_repo_search.databinding.FragmentReposBinding
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.network.dto.asDomainModel
import com.michs.github_repo_search.repository.GitHubReposRepository
import timber.log.Timber
import javax.inject.Inject

class ReposFragment: Fragment(){

    @Inject
    lateinit var repository: GitHubReposRepository
    private val viewModel: ReposViewModel by viewModels {ReposViewModelFactory(repository) }
    private lateinit var rv: RecyclerView
    private lateinit var loadingLayout: View

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
        (activity as MainActivity).collapsingToolbarLayout.title = "Search GitHub repositories"

        loadingLayout = binding.loadingLayout
        rv = binding.recyclerView

        rv.apply{
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.repositories.observe(this, Observer { result ->
            when(result?.status){
                Result.Status.SUCCESS -> {
                    loadingLayout.isVisible = false
                    Timber.d(result.data.toString())
                    binding.noResults.isVisible = result.data?.items.isNullOrEmpty()
                    result.data?.items?.let { adapter.submitList(it.asDomainModel()) }
                }
                Result.Status.ERROR -> {
                    loadingLayout.isVisible = false
                    Timber.d(result.message)
                    binding.noResults.isVisible = result.data?.items.isNullOrEmpty()
                    Toast.makeText(activity, "${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
        return binding.root
    }

    fun showLoadingLayout(){
        loadingLayout.isVisible = true
    }
}