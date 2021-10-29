package io.github.bradpatras.basicremoteconfigs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.net.HttpURLConnection
import java.net.URL

class BasicRemoteConfigs(private val remoteUrl: URL) {

    suspend fun fetchConfigs(): String? = withContext(Dispatchers.IO) {
        HttpHelper(remoteUrl).makeGetRequest()
    }
}