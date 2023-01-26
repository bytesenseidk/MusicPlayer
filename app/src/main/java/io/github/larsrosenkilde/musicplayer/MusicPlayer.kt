package io.github.larsrosenkilde.musicplayer

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import io.github.larsrosenkilde.musicplayer.services.PermissionsManager
import io.github.larsrosenkilde.musicplayer.services.radio.Radio
import io.github.larsrosenkilde.musicplayer.services.SettingsManager
import io.github.larsrosenkilde.musicplayer.services.database.Database
import io.github.larsrosenkilde.musicplayer.services.groove.GrooveManager
import io.github.larsrosenkilde.musicplayer.services.i18n.Translations
import io.github.larsrosenkilde.musicplayer.services.i18n.Translator

interface MusicHooks {
    fun onMusicReady() {}
    fun onMusicPause() {}
    fun onMusicDestroy() {}
}

class MusicPlayer(application: Application): AndroidViewModel(application), MusicHooks {
    val radio = Radio(this)
    val settings = SettingsManager(this)
    val permission = PermissionsManager(this)
    val groove = GrooveManager(this)
    val database = Database(this)
    val translator = Translator(this)
    val t: Translations get() = translator.t

    val applicationContext: Context
        get() = getApplication<Application>().applicationContext

    private var isReady = false
    private var hooks = listOf(this, radio, /*groove*/)

    fun ready() {
        if (isReady) return
        isReady = true
        notifyHooks { onMusicReady() }
    }

    fun pause() {
        notifyHooks { onMusicPause() }
    }

    fun destroy() {
        notifyHooks { onMusicDestroy() }
    }

    private fun notifyHooks(fn: MusicHooks.() -> Unit) {
        hooks.forEach { fn.invoke(it) }
    }
}