package com.wynneplaga.monsterhunterreference

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.turingtechnologies.materialscrollbar.AlphabetIndicator
import com.wynneplaga.monsterhunterreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adapter = ItemListAdapter()
        binding.mainContentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainContentRecyclerView.adapter = adapter
        binding.mainContentRecyclerView.setHasFixedSize(true)

        binding.swipeToRefreshLayout.isRefreshing = true    // Indicate that we start loading immediately
        binding.swipeToRefreshLayout.setColorSchemeColors(Color.BLUE, Color.parseColor("#FFA500"), Color.RED, Color.parseColor("#86B049"))
        binding.swipeToRefreshLayout.setOnRefreshListener(viewModel::refreshItemsList)

        // Add listener for searchbar
        val searchView = binding
            .topAppBar
            .menu
            .findItem(R.id.action_search)
            .actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchTerm.value = newText
                return true
            }
        })

        // My own library, which is available open source (note that it is a bit old though)
        binding.scrollBar.setIndicator(AlphabetIndicator(this), true)

        // Start loading the items list from the server
        viewModel.refreshItemsList()
        viewModel.filteredItems.observe(this@MainActivity) {
            // Update the scrollbar state whenever the list changes
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
                binding.scrollBar.requestLayout()

            binding.swipeToRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        }
    }

}