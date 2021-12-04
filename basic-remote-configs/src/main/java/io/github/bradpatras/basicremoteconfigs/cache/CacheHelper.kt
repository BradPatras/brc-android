package io.github.bradpatras.basicremoteconfigs.cache

import android.util.Log
import io.github.bradpatras.basicremoteconfigs.internal.BrcInitializer
import io.github.bradpatras.basicremoteconfigs.util.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*
import java.time.LocalDateTime
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

        fun getLastModified(): LocalDateTime? {
            return if (getCacheFile().exists()) {
                DateHelper.dateTimeFromEpochMillis(getCacheFile().lastModified())
            } else {
                null
            }
        }

        private fun getCacheFile(): File = File(BrcInitializer.filesDirectory, cacheFilename)
}