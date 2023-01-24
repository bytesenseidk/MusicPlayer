package io.github.larsrosenkilde.musicplayer.services

import android.media.metrics.Event
import io.github.larsrosenkilde.musicplayer.MusicHooks
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.ConcurrentList
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

    private var player: RadioPlayer? = null

    val hasPlayer: Boolean
        get() = player?.usable ?: false

    data class PlayOptions(
        val index: Int = 0,
        val autostart: Boolean = true,
        val startPosition: Int? = null
    )
}