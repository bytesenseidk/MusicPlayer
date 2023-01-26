package io.github.larsrosenkilde.musicplayer.services.radio

import io.github.larsrosenkilde.musicplayer.MusicPlayer

class RadioShorty(private val musicPlayer: MusicPlayer) {
    fun skip(): Boolean {
        if (!musicPlayer.radio.hasPlayer) return false
        return when {
            !musicPlayer.radio.hasPlayer -> false
            musicPlayer.radio.canJumpToNext() -> {
                musicPlayer.radio.jumpToNext()
                true
            }
            else -> {
                musicPlayer.radio.play(Radio.PlayOptions(index = 0, autostart = false))
                false
            }
        }
    }
}