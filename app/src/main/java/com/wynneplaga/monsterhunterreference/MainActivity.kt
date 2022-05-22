package com.wynneplaga.monsterhunterreference

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.wynneplaga.monsterhunterreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater).apply {
            viewModel = activityViewModel
            lifecycleOwner = this@MainActivity
            setContentView(root)

            val adapter = ItemListAdapter()
            mainContentRecyclerView.adapter = adapter
            swipeToRefreshLayout.setColorSchemeColors(Color.BLUE, Color.parseColor("#FFA500"), Color.RED, Color.parseColor("#86B049"))

            // Add listener for searchbar
            val searchView = topAppBar
                .menu
                .findItem(R.id.action_search)
                .actionView as SearchView
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = true

                override fun onQueryTextChange(newText: String): Boolean {
                    activityViewModel.searchTerm.value = newText
                    return true
                }
            })

            // Start loading the items list from the server
            activityViewModel.refreshItemsList()
        }
    }

}