package io.github.bradpatras.brc_android_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.bradpatras.basicremoteconfigs.BasicRemoteConfigs
import kotlinx.coroutines.*
import java.net.URL

private const val CONFIG_URL = "https://github.com/BradPatras/basic-remote-configs/raw/main/examples/simple.json"

class MainActivity : AppCompatActivity() {

    private val brc: BasicRemoteConfigs = BasicRemoteConfigs(URL(CONFIG_URL))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenCreated {
            delay(1000)

            try {
                brc.fetchConfigs()
                withContext(Dispatchers.Main) {
                    updateText(brc.values.map { "${it.key}: ${it.value}" }
                        .joinToString(separator = ",\n"))
                }
            } catch (error: Throwable) {
                withContext(Dispatchers.Main) {
                    updateText("Encountered an error when fetching configs")
                }
            }
        }
    }

    private fun updateText(newText: String?) {
        val textView = findViewById<TextView>(R.id.center_tv)
        textView.text = newText
    }
}