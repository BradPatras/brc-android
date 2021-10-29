package io.github.bradpatras.brc_android_demo

import android.content.res.AssetFileDescriptor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.bradpatras.basicremoteconfigs.BasicRemoteConfigs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val brc: BasicRemoteConfigs = BasicRemoteConfigs(URL("https://github.com/BradPatras/basic-remote-configs/raw/main/examples/simple.json"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenCreated {
            val configs = brc.fetchConfigs()
            withContext(Dispatchers.Main) {
                updateText(configs)
            }
        }
    }

    private fun updateText(newText: String?) {
        val textView = findViewById<TextView>(R.id.center_tv)
        textView.text = newText
    }
}