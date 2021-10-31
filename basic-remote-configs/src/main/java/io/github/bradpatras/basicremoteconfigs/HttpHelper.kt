package io.github.bradpatras.basicremoteconfigs

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HttpHelper(private val url: URL) {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun makeGetRequest(): String? = withContext(Dispatchers.IO) {
        try {
            val connection = url.openConnection() as? HttpsURLConnection
            connection?.connect()
            return@withContext connection?.inputStream?.bufferedReader()?.use(BufferedReader::readText)
        } catch (exception: IOException) {
            print("Error when executing get request: " + exception.localizedMessage)
            return@withContext null
        } catch (err: Error) {
            print("Error when executing get request: " + err.localizedMessage)
            return@withContext null
        }
    }
}