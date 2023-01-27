package io.github.larsrosenkilde.musicplayer.services.radio

import io.github.larsrosenkilde.musicplayer.MusicPlayer

class RadioShorty(private val musicPlayer: MusicPlayer) {
    fun playPause() {
        if (!musicPlayer.radio.hasPlayer) return
        when {
            musicPlayer.radio.isPlaying -> musicPlayer.radio.pause()
            else -> musicPlayer.radio.resume()
        }
    }

    fun previous(): Boolean {
        return when {
            !musicPlayer.radio.hasPlayer -> false
            musicPlayer.radio.currentPlaybackPosition!!.played <= 3000 && musicPlayer.radio.canJumpToPrevious() -> {
                musicPlayer.radio.jumpToPrevious()
                true
            }
            else -> {
                musicPlayer.radio.seek(0)
                false
            }
        }
    }

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