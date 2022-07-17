package io.github.bradpatras.basicremoteconfigs.network

import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

internal class HttpRequestHelper {
    companion object {
        @Suppress("BlockingMethodInNonBlockingContext")
        suspend fun makeGetRequest(
            url: URL,
            customHeaders: HashMap<String, String>
        ): JSONObject? = withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as? HttpsURLConnection
                customHeaders.map { connection?.setRequestProperty(it.key, it.value) }
                connection?.requestMethod = "GET"
                connection?.connect()
                val responseString = connection?.inputStream?.bufferedReader()?.use(BufferedReader::readText)

                return@withContext responseString?.let { JSONObject(it) }
            } catch (exception: Throwable) {
                Log.e("BasicRemoteConfigs", "Error when executing get request", exception)
                throw exception
            }
        }
    }
}