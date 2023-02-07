package io.github.larsrosenkilde.musicplayer.services.radio

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.services.groove.Song
import kotlin.random.Random

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

    fun playQueue(
        songIds: List<Long>,
        options: Radio.PlayOptions = Radio.PlayOptions(),
        shuffle: Boolean = false
    ) {
        musicPlayer.radio.stop(ended = false)
        musicPlayer.radio.queue.add(
            songIds,
            options = options.run {
                copy(index = if (shuffle) Random.nextInt(songIds.size) else options.index)
            }
        )
        if (shuffle) {
            musicPlayer.radio.queue.setShuffleMode(true)
        }
    }

    @JvmName("PlayQueueFromSongList")
    fun playQueue(
        songs: List<Song>,
        options: Radio.PlayOptions = Radio.PlayOptions(),
        shuffle: Boolean = false
    ) = playQueue(
        songIds = songs.map { it.id },
        options = options,
        shuffle = shuffle
    )

    fun playQueue(song: Song, shuffle: Boolean = false) = playQueue(listOf(song), shuffle = shuffle)
}