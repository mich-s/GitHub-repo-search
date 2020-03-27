package com.michs.github_repo_search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.michs.github_repo_search.databinding.ActivityMainBinding
import com.michs.github_repo_search.repos.ReposFragment
import com.michs.github_repo_search.repository.GitHubReposRepository
import com.michs.github_repo_search.utils.hideSoftKeyboard
import kotlinx.coroutines.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: GitHubReposRepository
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var textUpdatedJob: Job? = null
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var clearText: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        toolbar = binding.toolbar
        clearText = binding.searchClear
        collapsingToolbarLayout = binding.collapsingToolbar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(destinationChangedListener)
        binding.appBarLayout.addOnOffsetChangedListener(offsetChangeListener)

        clearText.setOnClickListener {
            binding.searchEditText.text = null
        }

        binding.searchEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                clearText.isVisible = searchText.isNotEmpty()
                if (searchText.isNotEmpty()) {
                    textUpdatedJob?.cancel()
                    textUpdatedJob = CoroutineScope(Dispatchers.IO).launch {
                        delay(500)
                        withContext(Dispatchers.Main){
                            val navHostFragment = supportFragmentManager.primaryNavigationFragment as? NavHostFragment
                            val reposFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment as? ReposFragment
                            reposFragment?.showLoadingLayout()
                        }
                        repository.searchRepositories(searchText)
                    }
                }
            }
        })

    }

    private val destinationChangedListener = NavController.OnDestinationChangedListener {
            navController, _, _ ->
        toolbar.setCollapsible(navController.currentDestination?.id == R.id.reposFragment)
        binding.appBarLayout.setExpanded(navController.currentDestination?.id == R.id.reposFragment)
    }

    private val offsetChangeListener = AppBarLayout.OnOffsetChangedListener{ _, verticalOffset ->
        when(verticalOffset){
            0 -> binding.searchCardView.isVisible = true
            else -> {
                binding.searchCardView.apply {
                    isVisible = false
                    hideSoftKeyboard(this@MainActivity, this)
                }

            }
        }
    }
}
