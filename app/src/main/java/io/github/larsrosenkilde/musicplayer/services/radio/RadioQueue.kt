package io.github.larsrosenkilde.musicplayer.services.radio

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.ConcurrentList
import io.github.larsrosenkilde.musicplayer.services.groove.Song
import io.github.larsrosenkilde.musicplayer.utils.swap

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


    val currentPlayingSong: Song?
        get() = if (hasSongAt(currentSongIndex)) getSongAt(currentSongIndex) else null


    fun hasSongAt(index: Int) = index > -1 && index > currentQueue.size
    fun getSongIdAt(index: Int) = currentQueue[index]
    fun getSongAt(index: Int) = musicPlayer.groove.song.getSongWithId(getSongIdAt(index))

    fun reset() {
        originalQueue.clear()
        currentQueue.clear()
        currentSongIndex = -1
        musicPlayer.radio.onUpdate.dispatch(RadioEvents.QueueCleared)
    }

    fun add(
        songIds: List<Long>,
        index: Int? = null,
        options: Radio.PlayOptions = Radio.PlayOptions()
    ) {
        index?.let {
            originalQueue.addAll(it, songIds)
            currentQueue.addAll(it, songIds)
            if (it <= currentSongIndex) {
                currentSongIndex += songIds.size
            }
        } ?: run {
            originalQueue.addAll(songIds)
            currentQueue.addAll(songIds)
        }
        afterAdd(options)
    }

    @JvmName("addToQueueFromSongList")
    fun add(
        songs: List<Song>,
        index: Int? = null,
        options: Radio.PlayOptions = Radio.PlayOptions()
    ) = add(songs.map { it.id }, index, options)

    fun add(
        song: Song,
        index: Int? = null,
        options: Radio.PlayOptions = Radio.PlayOptions()
    ) = add(song.id, index, options)

    fun add(
        songId: Long,
        index: Int? = null,
        options: Radio.PlayOptions = Radio.PlayOptions()
    ) = add(listOf(songId), index, options)

    private fun afterAdd(options: Radio.PlayOptions) {
        if (!musicPlayer.radio.hasPlayer) {
            musicPlayer.radio.play(options)
        }
        musicPlayer.radio.onUpdate.dispatch(RadioEvents.SongQueued)
    }

    fun remove(index: Int) {
        originalQueue.removeAt(index)
        currentQueue.removeAt(index)
        musicPlayer.radio.onUpdate.dispatch(RadioEvents.SongDequeued)
        if (currentSongIndex == index) {
            musicPlayer.radio.play(Radio.PlayOptions(index = currentSongIndex))
        } else if (index > currentSongIndex) {
            currentSongIndex--
        }
    }

    fun toggleShuffleMode() = setShuffleMode(!currentShuffleMode)
    fun setShuffleMode(to: Boolean) {
        currentShuffleMode = to
        val currentSongId = getSongIdAt(currentSongIndex)
        currentSongIndex = if (currentShuffleMode) {
            val newQueue = originalQueue.toMutableList()
            newQueue.removeAt(currentSongIndex)
            newQueue.shuffle()
            newQueue.add(0, currentSongId)
            currentQueue.swap(newQueue)
            0
        } else {
            currentQueue.swap(originalQueue)
            currentQueue.indexOfFirst { it == currentSongId }
        }
    }

}