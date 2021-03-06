package com.michs.github_repo_search.repos


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michs.github_repo_search.databinding.ReposListItemBinding
import com.michs.github_repo_search.domain.Repository

class ReposAdapter(private val callback: RepoClick): ListAdapter<Repository, ReposAdapter.ViewHolder>(ReposDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ReposListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = getItem(position)
        holder.bind(repository, callback)
    }

    class ViewHolder constructor(private val binding: ReposListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(repository: Repository, callback: RepoClick){
            binding.apply {
                this.repository = repository
                this.repoClick = callback
            }
        }
    }

}

class RepoClick(val block: (Repository) -> Unit){
    fun onItemClick(repository: Repository) = block(repository)
}

private class ReposDiffCallback: DiffUtil.ItemCallback<Repository>(){
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean = oldItem.fullName == newItem.fullName
    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean = oldItem == newItem
}