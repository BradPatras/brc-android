package io.github.bradpatras.basicremoteconfigs

import kotlinx.coroutines.coroutineScope
import java.net.URL

class BasicRemoteConfigs(private val remoteUrl: URL) {

    suspend fun fetchConfigs(): String? = coroutineScope {
        HttpRequestHelper(remoteUrl).makeGetRequest()
    }

    fun getBoolean(key: String): Boolean? {
        return true
    }

    fun getInt(key: String): Int? {
        return 0
    }

    fun getString(key: String): String? {
        return "true"
    }

    fun getBooleanArray(key: String): Array<Boolean>? {
        return emptyArray()
    }

    fun getIntArray(key: String): Array<Int>? {
        return emptyArray()
    }

    fun getStringArray(key: String): Array<String>? {
        return emptyArray()
    }
}