package io.github.bradpatras.basicremoteconfigs

import android.content.SharedPreferences
import androidx.annotation.RawRes
import java.net.URL

class BasicRemoteConfigs {
    private val remoteUrl: URL
    private val localConfig: Unit

    constructor(remoteUrl: URL, localAssetConfigName: String) {
        this.remoteUrl = remoteUrl
        this.localConfig = Unit
    }

    constructor(remoteUrl: URL, @RawRes localRawConfigResId: Int) {
        this.remoteUrl = remoteUrl
        this.localConfig = Unit
    }
}