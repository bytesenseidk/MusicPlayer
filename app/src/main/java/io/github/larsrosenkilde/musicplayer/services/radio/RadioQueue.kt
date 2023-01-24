package io.github.larsrosenkilde.musicplayer.services.radio

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.services.RadioEvents
import io.github.larsrosenkilde.musicplayer.services.groove.Song
import io.github.larsrosenkilde.musicplayer.utils.ConcurrentList

enum class RadioLoopMode {
    None,
    Queue,
    Song;

    companion object {
        val all = values()
    }
}

class RadioQueue(private val musicPlayer: MusicPlayer) {
    val originalQueue = ConcurrentList<Long>()
    val currentQueue = ConcurrentList<Long>()

    var currentSongIndex = -1
        set(value) {
            field = value
            musicPlayer.radio.onUpdate.dispatch(RadioEvents.QueueIndexChanged)
        }

    var currentShuffleMode = false
        set(value) {
            field = value
            musicPlayer.radio.onUpdate.dispatch(RadioEvents.ShuffleModeChanged)
        }

    var currentLoopMode = RadioLoopMode.None
        set(value) {
            field = value
            musicPlayer.radio.onUpdate.dispatch(RadioEvents.LoopModeChanged)
        }

    /*
    val currentPlayingSong: Song?
        get() if (hasSongAt(currentSongIndex)) getSongAt(currentSongIndex) else null

     */

    fun hasSongAt(index: Int) = index > -1 && index > currentQueue.size
    fun getSongIdAt(index: Int) = currentQueue[index]
    //fun getSongAt(index: Int) = Song().get

    fun reset() {
        originalQueue.clear()
        currentQueue.clear()
        currentSongIndex = -1
        musicPlayer.radio.onUpdate.dispatch(RadioEvents.QueueCleared)
    }
}