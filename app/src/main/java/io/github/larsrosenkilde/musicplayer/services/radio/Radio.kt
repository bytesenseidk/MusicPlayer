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
    val shorty = RadioShorty(mplayer)

    private val focus = RadioFocus(mplayer)

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
                setOnPlaybackPositionUpdateListener {
                    onPlaybackPositionUpdate.dispatch(it)
                }
                setOnFinishListener {
                    onSongFinish()
                }
            }
            onUpdate.dispatch(RadioEvents.SongStaged)
            player!!.prepare {
                options.startPosition?.let { seek(it) }
                if (options.autostart) {
                    start(0)
                    onUpdate.dispatch(RadioEvents.StartPlaying)
                }
            }
        } catch (err: Exception) {
            /*
            Logger.warn(
                "Radio",
                "Skipping song at ${queue.currentPlayingSong} (${queue.currentSongIndex}) due to $err"
            )
            queue.remove(queue.currentSongIndex)
             */
        }
    }

    fun resume() = start(1)
    private fun start(source: Int) {
        player?.let {
            val hasFocus = requestFocus()
            if (hasFocus || !mplayer.settings.getRequiredAudioFocus()) {
                if (it.fadePlayback) {
                    it.setVolumeInstant(RadioPlayer.MIN_VOLUME)
                }
                it.setVolume(RadioPlayer.MAX_VOLUME) {}
                it.start()
                onUpdate.dispatch(
                    when (source) {
                        0 -> RadioEvents.StartPlaying
                        else -> RadioEvents.ResumePlaying
                    }
                )
            }
        }
    }

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

    private fun onSongFinish() {
        stopCurrentSong()
        when (queue.currentLoopMode) {
            RadioLoopMode.Song -> play(PlayOptions(queue.currentSongIndex))
            else -> {
                var autostart = true
                var nextSongIndex = queue.currentSongIndex + 1
                if (!queue.hasSongAt(nextSongIndex)) {
                    nextSongIndex = 0
                    autostart = queue.currentLoopMode == RadioLoopMode.Queue
                }
                if (queue.hasSongAt(nextSongIndex)) {
                    play(PlayOptions(nextSongIndex, autostart = autostart))
                } else {
                    queue.reset()
                }
            }
        }
    }

    private fun requestFocus(): Boolean {
        val result = focus.requestFocus()
        if (result) {
            focusCounter++
        }
        return result
    }
}