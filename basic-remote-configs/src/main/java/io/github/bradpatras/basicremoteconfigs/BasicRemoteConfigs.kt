package io.github.bradpatras.basicremoteconfigs

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

// Version code representing an unknown or un-fetched version
private const val VERSION_NONE = -1

// Json key for the config version
private const val VERSION_KEY = "ver"

/**
 * Basic remote configs
 *
 * @property remoteUrl
 * @constructor Create empty Basic remote configs
 */
class BasicRemoteConfigs(private val remoteUrl: URL) {
    private var _version: Int = VERSION_NONE
    private val _valuesFlow: MutableStateFlow<HashMap<String, Any>> = MutableStateFlow(HashMap())

    /**
     * Hash map containing the config values
     */
    val values: HashMap<String, Any> get() = valuesFlow.value

    /**
     * Flow version of the config values. Will emit new values after successfully
     */
    val valuesFlow: StateFlow<HashMap<String, Any>> = _valuesFlow

    /**
     * Version parsed from the fetched configs.  If configs haven't been fetched or
     * there was no version key included, the version will default to *-1*
     */
    val version: Int get() = _version

    /**
     * A Date representing the last time the configs were successfully fetched and updated.
     */
    var fetchDate: Date? = null

    /**
     * Fetch configs from **url** provided in class constructor.
     * If configs are fetched successfully and contain a **version** value
     * different from what is currently stored, a new value will be emitted
     * from the **valuesFlow** property.
     */
    suspend fun fetchConfigs(): Unit = coroutineScope {
        val configs = HttpRequestHelper(remoteUrl).makeGetRequest() ?: ""
        try {
            val jsonObject = JSONObject(configs)
            val newVersion = jsonObject[VERSION_KEY] as? Int ?: VERSION_NONE
            val newValues = jsonObject.toMap()

            // Do not emit a new value if the version hasn't changed
            if (newVersion != _version) {
                fetchDate = Date()
                _valuesFlow.emit(newValues)
            }
        } catch (e: Throwable) {
            Log.e("BasicRemoteConfigs", "Failed to parse config json.", e)
        }
    }

    /**
     * Get keys of all current configs
     *
     * @return Set containing config keys
     */
    fun getKeys(): Set<String> {
        return valuesFlow.value.keys
    }

    /**
     * Get boolean
     *
     * @param key
     * @return Boolean value associated with key, null if key or value doesn't exist.
     */
    fun getBoolean(key: String): Boolean? {
        return valuesFlow.value[key] as? Boolean?
    }

    /**
     * Get int
     *
     * @param key
     * @return Int value associated with key, null if key or value doesn't exist.
     */
    fun getInt(key: String): Int? {
        return valuesFlow.value[key] as? Int
    }

    /**
     * Get string
     *
     * @param key
     * @return String value associated with key, null if key or value doesn't exist.
     */
    fun getString(key: String): String? {
        return valuesFlow.value[key] as? String
    }

    /**
     * Get boolean array
     *
     * @param key
     * @return Boolean array associated with key, null if key or value doesn't exist.
     */
    fun getBooleanArray(key: String): Array<Boolean>? {
        val jsonArray = valuesFlow.value[key] as? JSONArray ?: return null
        val values = mutableListOf<Boolean>()
        for (i in 0 until jsonArray.length()) {
            (jsonArray[i] as? Boolean)?.let { values.add(it) }
        }

        return values.toTypedArray()
    }

    /**
     * Get int array
     *
     * @param key
     * @return Int array associated with key, null if key or value doesn't exist.
     */
    fun getIntArray(key: String): Array<Int>? {
        val jsonArray = valuesFlow.value[key] as? JSONArray ?: return null
        val values = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()) {
            (jsonArray[i] as? Int)?.let { values.add(it) }
        }

        return values.toTypedArray()
    }

    /**
     * Get string array
     *
     * @param key
     * @return String array associated with key, null if key or value doesn't exist.
     */
    suspend fun getStringArray(key: String): Array<String>? {
        val jsonArray = valuesFlow.value[key] as? JSONArray ?: return null
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