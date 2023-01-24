package io.github.larsrosenkilde.musicplayer

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import io.github.larsrosenkilde.musicplayer.services.Radio
import io.github.larsrosenkilde.musicplayer.services.SettingsManager

interface MusicHooks {
    fun onMusicReady() {}
    fun onMusicPause() {}
    fun onMusicDestroy() {}
}

class MusicPlayer(application: Application): AndroidViewModel(application), MusicHooks {
    val radio = Radio(this)
    //val groove = GrooveManager(this)
    val settings = SettingsManager(this)
    val applicationContext: Context
        get() = getApplication<Application>().applicationContext

    private var isReady = false
    private var hooks = listOf(this, radio, /*groove*/)

    fun ready() {
        if (isReady) return
        isReady = true
        notifyHooks { onMusicReady() }
    }

    private fun notifyHooks(fn: MusicHooks.() -> Unit) {
        hooks.forEach { fn.invoke(it) }
    }
}