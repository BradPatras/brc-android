package io.github.bradpatras.basicremoteconfigs

import kotlinx.coroutines.coroutineScope
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

// Version code representing an unknown or un-fetched version
private const val VERSION_NONE = -1

class BasicRemoteConfigs(private val remoteUrl: URL) {
    private var version: Int = VERSION_NONE
    private var values: HashMap<String, Any> = HashMap()
    private var expirationDate: Date? = null

    suspend fun fetchConfigs() = coroutineScope {
        val configs = HttpRequestHelper(remoteUrl).makeGetRequest() ?: ""
        val json = JSONObject(configs)
        val newValues = HashMap<String, Any>()
        json.keys().forEach { newValues[it] = json.get(it) }
        values = newValues
        expirationDate = getNewExpirationDate()
    }

    private fun getNewExpirationDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 1)
        return calendar.time
    }

    private suspend fun getCurrentValues() = coroutineScope {
        if (expirationDate?.after(Date()) != true || values.isEmpty()) {
            fetchConfigs()
        }

        return@coroutineScope values
    }

    suspend fun getBoolean(key: String): Boolean? {
        return getCurrentValues()[key] as? Boolean?
    }

    suspend fun getInt(key: String): Int? {
        return getCurrentValues()[key] as? Int
    }

    suspend fun getString(key: String): String? {
        return getCurrentValues()[key] as? String
    }

    suspend fun getBooleanArray(key: String): Array<Boolean>? {
        val jsonArray = getCurrentValues()[key] as? JSONArray ?: return null
        val values = mutableListOf<Boolean>()
        for (i in 0 until jsonArray.length()) {
            (jsonArray[i] as? Boolean)?.let { values.add(it) }
        }

        return values.toTypedArray()
    }

    suspend fun getIntArray(key: String): Array<Int>? {
        val jsonArray = getCurrentValues()[key] as? JSONArray ?: return null
        val values = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()) {
            (jsonArray[i] as? Int)?.let { values.add(it) }
        }

        return values.toTypedArray()
    }

    suspend fun getStringArray(key: String): Array<String>? {
        val jsonArray = getCurrentValues()[key] as? JSONArray ?: return null
        val values = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            (jsonArray[i] as? String)?.let { values.add(it) }
        }

        return values.toTypedArray()
    }
}