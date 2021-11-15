package io.github.bradpatras.basicremoteconfigs

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

// Version code representing an unknown or un-fetched version
private const val VERSION_NONE = -1
private const val VERSION_KEY = "version"

class BasicRemoteConfigs(private val remoteUrl: URL) {
    private var _version: Int = VERSION_NONE
    private var expirationDate: Date? = null
    private val _valuesFlow: MutableStateFlow<HashMap<String, Any>> = MutableStateFlow(HashMap())

    val valuesFlow: StateFlow<HashMap<String, Any>> = _valuesFlow
    val version: Int get() = _version

    suspend fun fetchConfigs() = coroutineScope {
        val configs = HttpRequestHelper(remoteUrl).makeGetRequest() ?: ""
        val jsonObject = JSONObject(configs)
        val newVersion = jsonObject[VERSION_KEY] as? Int ?: VERSION_NONE
        val newValues = jsonObject.toMap()
        expirationDate = getNewExpirationDate()

        // Do not emit a new value if the version hasn't changed
        if (newVersion != _version) {
            _valuesFlow.emit(newValues)
        }
    }

    private fun getNewExpirationDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 1)
        return calendar.time
    }

    private suspend fun getCurrentValues() = coroutineScope {
        val expired = expirationDate?.after(Date()) != true || valuesFlow.value.isEmpty()
        val versionNone = version == VERSION_NONE

        if (expired or versionNone) {
            fetchConfigs()
        }

        return@coroutineScope valuesFlow.value
    }

    suspend fun getKeys(): Set<String> {
        return getCurrentValues().keys
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

private fun JSONObject.toMap(): HashMap<String, Any> {
    return HashMap<String, Any>().also { map ->
        keys().forEach { key ->
            map[key] = this.get(key)
        }
    }
}