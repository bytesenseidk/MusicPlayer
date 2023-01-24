package io.github.larsrosenkilde.musicplayer.services.radio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.github.larsrosenkilde.musicplayer.MusicPlayer

class RadioNativeReceiver(private val musicPlayer: MusicPlayer): BroadcastReceiver() {
    fun start() {
        musicPlayer.applicationContext.registerReceiver(
            this,
            IntentFilter().apply {
                addAction(Intent.ACTION_HEADSET_PLUG)
            }
        )
    }
    fun destroy() {
        musicPlayer.applicationContext.unregisterReceiver(this)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.let { action ->
            when (action) {
                Intent.ACTION_HEADSET_PLUG -> {
                    intent.extras?.getInt("state")?.let {
                        when (it) {
                            0 -> onHeadphonesDisconnect()
                            1 -> onHeadphonesConnect()
                            else -> {}
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun onHeadphonesConnect() {
        if (!musicPlayer.radio.hasPlayer) return
        if (!musicPlayer.radio.isPlaying && musicPlayer.settings.getPlayOnHeadphonesConnect()) {
            musicPlayer.radio.resume()
        }
    }
    private fun onHeadphonesDisconnect() {
        if (!musicPlayer.radio.hasPlayer) return
        if (musicPlayer.radio.isPlaying && musicPlayer.settings.getPauseOnHeadphonesDisconnect())
    }
}