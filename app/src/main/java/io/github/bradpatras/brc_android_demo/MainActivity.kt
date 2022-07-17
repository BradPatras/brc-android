package io.github.bradpatras.brc_android_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import io.github.bradpatras.basicremoteconfigs.BasicRemoteConfigs
import io.github.bradpatras.brc_android_demo.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.net.URL

private const val CONFIG_URL = "https://github.com/BradPatras/basic-remote-configs/raw/main/examples/simple.json"

class MainActivity : AppCompatActivity() {

    private val brc: BasicRemoteConfigs = BasicRemoteConfigs(URL(CONFIG_URL))
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fetchBtn.setOnClickListener {
            updateUI(null, true)
            lifecycleScope.launch {
                getConfigs()
            }
        }

        binding.clearBtn.setOnClickListener {
            updateUI(null, false)
            brc.clearCache()
        }
    }

    private suspend fun getConfigs() = coroutineScope {
        try {
            brc.fetchConfigs()
            withContext(Dispatchers.Main) {
                updateUI(
                    brc.values.map { "${it.key}: ${it.value}" }.joinToString(separator = ",\n"),
                    false
                )
            }
        } catch (error: Throwable) {
            withContext(Dispatchers.Main) {
                updateUI("Encountered an error when fetching configs", false)
            }
        }
    }

    private fun updateUI(newText: String?, isLoading: Boolean) {
        binding.centerTv.text = newText
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}