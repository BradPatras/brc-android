package io.github.bradpatras.basicremoteconfigs

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

internal class HttpRequestHelper(private val url: URL) {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun makeGetRequest(): String? = withContext(Dispatchers.IO) {
        return@withContext try {
            val connection = url.openConnection() as? HttpsURLConnection
            connection?.connect()
            connection?.inputStream?.bufferedReader()?.use(BufferedReader::readText)
        } catch (exception: Throwable) {
            Log.e("BasicRemoteConfigs", "Error when executing get request", exception)
            null
        }
    }
}