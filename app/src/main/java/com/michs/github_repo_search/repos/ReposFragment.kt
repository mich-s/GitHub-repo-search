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
import com.michs.github_repo_search.databinding.FragmentReposBinding
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.network.dto.asDomainModel
import com.michs.github_repo_search.repository.GitHubReposRepository
import com.michs.github_repo_search.utils.hideSoftKeyboard
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class ReposFragment: Fragment(){

    @Inject
    lateinit var repository: GitHubReposRepository

    private val viewModel: ReposViewModel by viewModels {ReposViewModelFactory(repository) }
    private var textUpdatedJob: Job? = null

    var search = ""

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

        val adapter = ReposAdapter(RepoClick {
            val direction = ReposFragmentDirections.actionReposFragmentToRepoDetailsFragment(it.fullName)
            findNavController().navigate(direction)
        })



        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        binding.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                when(verticalOffset){
                    0 -> binding.searchCardView.isVisible = true
                    else -> {
                        binding.searchCardView.apply {
                            isVisible = false
                            hideSoftKeyboard(activity, this)
                        }

                    }
                }
            }
        })


        binding.searchEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText.isNotEmpty()) {
                    search = searchText
                    textUpdatedJob?.cancel()
                    textUpdatedJob = GlobalScope.launch {
                        delay(500)
                        withContext(Dispatchers.Main){
                            binding.loadingLayout.isVisible = true
                        }
                        viewModel.searchRepositories(searchText)
                    }
                }
                else
                    Toast.makeText(activity, "Enter keyword to search repos", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.repositories.observe(this, Observer { result ->
            when(result?.status){
                Result.Status.SUCCESS -> {
                    binding.loadingLayout.isVisible = false
                    Timber.d(result.data.toString())
                    result.data?.items?.let { adapter.submitList(it.asDomainModel()) }
                }
                Result.Status.ERROR -> {
                    binding.loadingLayout.isVisible = false
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