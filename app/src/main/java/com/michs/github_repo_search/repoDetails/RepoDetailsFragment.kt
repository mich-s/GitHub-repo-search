package com.michs.github_repo_search.repoDetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.michs.github_repo_search.App
import com.michs.github_repo_search.databinding.FragmentReposBinding
import com.michs.github_repo_search.repository.GitHubReposRepository
import timber.log.Timber
import javax.inject.Inject

class RepoDetailsFragment: Fragment(){

    @Inject
    lateinit var repository: GitHubReposRepository
    private val args: RepoDetailsFragmentArgs by navArgs()
    private val viewModel: RepoDetailsViewModel by viewModels { RepoDetailsViewModelFactory(repository, args.fullName) }
    private lateinit var repoFullName: String

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

        repoFullName = viewModel.fullName
        Timber.d("repofullName: $repoFullName")

        return binding.root
    }
}