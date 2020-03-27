package com.michs.github_repo_search.repoDetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.michs.github_repo_search.App
import com.michs.github_repo_search.MainActivity
import com.michs.github_repo_search.databinding.FragmentRepoDetailBinding
import com.michs.github_repo_search.domain.Repository
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.network.dto.asDomainObject
import com.michs.github_repo_search.repository.GitHubReposRepository
import com.michs.github_repo_search.utils.formatDate
import kotlinx.android.synthetic.main.fragment_repo_detail.*
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
        val binding = FragmentRepoDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        repoFullName = viewModel.fullName

        viewModel.repoDetail.observe(viewLifecycleOwner, Observer { result ->
            when(result?.status){
                Result.Status.SUCCESS -> {
                    Timber.d("${result.data?.toString()}")
                    bindRepository(result.data!!.asDomainObject())
                }
                Result.Status.ERROR -> {
                    Timber.d("${result.message}")
                    Toast.makeText(activity, "${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
        (activity as MainActivity).collapsingToolbarLayout.title = args.fullName.split("/")[1]
        return binding.root
    }

    private fun bindRepository(repository: Repository){
        Glide.with(image.context).load(repository.owner.avatarUrl).into(image)
        description.text = repository.description
        user.text = repository.name
        created_at.text = repository.createdAt.formatDate()
        updated_at.text = repository.updatedAt.formatDate()
        language.text = repository.language
        stars.text = repository.stargazersCount.toString()
        forks.text = repository.forksCount.toString()
    }
}