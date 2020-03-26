package com.michs.github_repo_search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.michs.github_repo_search.databinding.ActivityMainBinding
import com.michs.github_repo_search.repository.GitHubReposRepository
import com.michs.github_repo_search.utils.hideSoftKeyboard
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: GitHubReposRepository
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var textUpdatedJob: Job? = null

    private val listener = NavController.OnDestinationChangedListener {
            navController, _, _ ->
        toolbar.setCollapsible(navController.currentDestination?.id == R.id.reposFragment)
        binding.appBarLayout.setExpanded(navController.currentDestination?.id == R.id.reposFragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(listener)

//        appBarConfiguration = AppBarConfiguration(setOf(R.id.reposFragment, R.id.repoDetailsFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
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
        })

        binding.searchEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText.isNotEmpty()) {
                    var search = searchText
                    textUpdatedJob?.cancel()
                    textUpdatedJob = GlobalScope.launch {
                        delay(500)
//                        withContext(Dispatchers.Main){
//                            binding.loadingLayout.isVisible = true
//                        }
                        repository.searchRepositories(searchText)
                    }
                }
                else
                    Toast.makeText(this@MainActivity, "Enter keyword to search repos", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
