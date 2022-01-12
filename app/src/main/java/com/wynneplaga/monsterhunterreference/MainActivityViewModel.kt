package com.wynneplaga.monsterhunterreference

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivityViewModel: ViewModel() {

    private val items = MutableLiveData<List<ItemModel>>()
    val searchTerm = MutableLiveData<String>()  // The term we are searching by

    // Process the list of items from above, filtered by the search term
    val filteredItems = MediatorLiveData<List<ItemModel>>().apply {
        var filterJob: Job? = null
        // The block that actually does the filtering
        val filterFunction: (Any) -> Unit = {
            // Cancel any currently running filter if the search term or item list is update before it completes
            filterJob?.cancel()
            // If the search term is blank, don't filter at all
            if (searchTerm.value?.isEmpty() != false) {
                value = items.value ?: emptyList()
            } else {
                // Otherwise run the filter in the background
                // (even though the list filters quickly now, this approach would work well for even a very large list or on a budget device)
                filterJob = Job()
                (viewModelScope + filterJob!!).launch(Dispatchers.Main) {
                    value = runInterruptible(Dispatchers.Default) {
                        items.value?.filter { it.name.lowercase().contains(searchTerm.value?.lowercase() ?: return@filter true) } ?: emptyList()
                    }
                }
            }
        }
        // Always run the filter, no matter which source changes
        addSource(items, filterFunction)
        addSource(searchTerm, filterFunction)
    }

    // The job that takes care of fetching the item list from the server
    var listFetchJob: CompletableJob? = null

    fun refreshItemsList() {
        // If we're already fetching the list, don't start again
        if (listFetchJob?.isActive == true) return

        listFetchJob = Job()
        (viewModelScope + listFetchJob!!).launch(Dispatchers.IO) {
            // Fetch the list
            val request = Request.Builder()
                .url("https://mhw-db.com/armor")
                .build()
            val result = OkHttpClient().newCall(request).execute().body?.string() ?: return@launch

            if (!isActive) return@launch    // Cooperative cancellation

            // Decode from JSON into the model objects
            val list = withContext(Dispatchers.Default) {
                val moshi = Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val listAdapter: JsonAdapter<List<ItemModel>> = moshi.adapter(Types.newParameterizedType(List::class.java, ItemModel::class.java))
                listAdapter.fromJson(result)?.sorted()
            }

            if (!isActive) return@launch    // Cooperative cancellation

            // Assign the result
            withContext(Dispatchers.Main) {
                items.value = list
                listFetchJob?.complete()
            }
        }
    }

}