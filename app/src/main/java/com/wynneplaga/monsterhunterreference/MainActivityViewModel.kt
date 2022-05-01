package com.wynneplaga.monsterhunterreference

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val items = MutableStateFlow(listOf<ItemModel>())
    val searchTerm = MutableStateFlow("")  // The term we are searching by

    // Filter the items by the search term. Run only while subscribed
    val filteredItems: StateFlow<List<ItemModel>> = items.combine(searchTerm) { items, searchTerm ->
        items.filter { it.name.contains(searchTerm, ignoreCase = true) }
    }.flowOn(Dispatchers.Default).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val isRefreshing = MutableStateFlow(false)

    fun refreshItemsList() = viewModelScope.launch {
        // Return if we're already refreshing
        if (!isRefreshing.compareAndSet(expect = false, update = true)) return@launch

        runCatching { items.value = MonsterHunterDataSource.getArmorList() }.onFailure {
            Toast.makeText(getApplication(), "Error fetching list: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        isRefreshing.value = false
    }

}