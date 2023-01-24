package io.github.larsrosenkilde.musicplayer.services.radio

import io.github.larsrosenkilde.musicplayer.MusicHooks
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer

enum class RadioEvents {
    StartPlaying,
    StopPlaying,
    PausePlaying,
    ResumePlaying,
    SongSeeked,
    SongQueued,
    SongDequeued,
    QueueIndexChanged,
    QueueModified,
    LoopModeChanged,
    ShuffleModeChanged,
    SongStaged,
    QueueCleared,
    QueueEnded
}

class Radio(private val mplayer: MusicPlayer): MusicHooks {
    val onUpdate = Eventer<RadioEvents>()
    val queue = RadioQueue(mplayer)

    private var player: RadioPlayer? = null
    private val nativeReceiver = RadioNativeReceiver(mplayer)
    val hasPlayer: Boolean
        get() = player?.usable ?: false
    val isPlaying: Boolean
        get() = player?.isPlaying ?: false
    val currentPlaybackPosition: PlaybackPosition?
        get() = player?.playbackPosition

    val onPlaybackPositionUpdate = Eventer<PlaybackPosition>()

    /*
    init {
        nativeReceiver.start()
    }

    fun destroy() {
        stop()
        notification.destroy()
        nativeReceiver.destroy()
    }
     */

    data class PlayOptions(
        val index: Int = 0,
        val autostart: Boolean = true,
        val startPosition: Int? = null
    )

    fun play(options: PlayOptions) {
        stopCurrentSong()
        if (!queue.hasSongAt(options.index)) {
            queue.currentSongIndex = -1
            return
        }
        val song = queue.getSongAt(options.index)!!
        queue.currentSongIndex = options.index
        try {
            player = RadioPlayer(mplayer, song.uri).apply {

            }
        }
    }

    fun resume() = start(1)

    private fun stopCurrentSong() {
        player?.let {
            player = null
            it.setOnPlaybackPositionUpdateListener {}
            it.setVolume(RadioPlayer.MIN_VOLUME) { _ ->
                it.stop()
                onUpdate.dispatch(RadioEvents.StopPlaying)
            }
        }
    }
}