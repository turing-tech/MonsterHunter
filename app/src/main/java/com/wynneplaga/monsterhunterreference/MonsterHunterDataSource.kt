package com.wynneplaga.monsterhunterreference

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object MonsterHunterDataSource {

    private const val endpoint = "https://mhw-db.com"
    private val networkClient = OkHttpClient()

    @Suppress("BlockingMethodInNonBlockingContext") // IOException throw is noted
    @Throws(IOException::class)
    suspend fun getArmorList(): List<ItemModel> {
        val responseString: String
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url("$endpoint/armor").build()
            val response = networkClient.newCall(request).execute()
            responseString = response.body?.string() ?: throw IOException("Response was null")
        }
        val resultList: List<ItemModel>
        withContext(Dispatchers.Default) {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val listAdapter: JsonAdapter<List<ItemModel>> = moshi.adapter(Types.newParameterizedType(List::class.java, ItemModel::class.java))
            resultList = listAdapter.fromJson(responseString)?.sorted() ?: throw IOException("Unable to create model list")
        }
        return resultList
    }

}