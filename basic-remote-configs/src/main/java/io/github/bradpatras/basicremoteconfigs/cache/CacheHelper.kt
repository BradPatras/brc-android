package io.github.bradpatras.basicremoteconfigs.cache

import android.util.Log
import io.github.bradpatras.basicremoteconfigs.internal.BrcInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*
import java.util.*

internal class CacheHelper(private val cacheFilename: String) {
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getCacheConfigs(): JSONObject? = withContext(Dispatchers.IO) {
        val cacheFile = getCacheFile()

        // Return early if cache file doesn't exist
        if (!cacheFile.exists()) return@withContext null

        try {
            val fileString = FileInputStream(cacheFile).use {
                return@use it.readBytes().decodeToString()
            }

            return@withContext JSONObject(fileString)
        } catch (exception: Throwable) {
            Log.e("BasicRemoteConfigs", "Error when reading from cache", exception)
            throw exception
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun setCacheConfigs(configsJson: JSONObject): Unit = withContext(Dispatchers.IO) {
        val cacheFile = getCacheFile()

        // Create if cache file doesn't exist
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        try {
            FileOutputStream(cacheFile).use {
                it.write(configsJson.toString().encodeToByteArray())
            }
        } catch (exception: Throwable) {
            Log.e("BasicRemoteConfigs", "Error when writing to cache", exception)
            throw exception
        }
    }

    fun getLastModified(): Long? {
        return if (getCacheFile().exists()) {
            getCacheFile().lastModified()
        } else {
            null
        }
    }

    fun deleteCacheFile() {
        val cacheFile = getCacheFile()
        cacheFile.delete()
    }

    private fun getCacheFile(): File = File(BrcInitializer.filesDirectory, cacheFilename)
}