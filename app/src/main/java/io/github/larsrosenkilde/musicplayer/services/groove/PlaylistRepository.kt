package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import java.util.concurrent.ConcurrentHashMap

enum class PlaylistSortBy {
    TITLE,
    TRACKS_COUNT
}

class PlaylistRepository(private val musicPlayer: MusicPlayer) {
    private val cached = ConcurrentHashMap<String, Playlist>()
}