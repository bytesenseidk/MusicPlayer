package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import java.util.concurrent.ConcurrentHashMap

class SongRepository(private val musicPlayer: MusicPlayer) {
    private val cached = ConcurrentHashMap<Long, Song>()
    private val cachedAlbumArtist = ConcurrentHashMap<String, MutableSet<Long>>()
    internal var cachedGenres = ConcurrentHashMap<String, Genre>()
    internal var cachedPaths = ConcurrentHashMap<String, Long>()
    var isUpdating = false

    fun getSongWithId(songId: Long) = cached[songId]
    fun hasSongWithId(songId: Long) = getSongWithId(songId) != null

    fun getAlbumArtistNames() = cachedAlbumArtist.keys.toList()
    fun getAlbumIdsOfAlbumArtist(artistName: String) =
        cachedAlbumArtist[artistName]?.toList() ?: listOf()

    fun fetch() {
        if (isUpdating) return
        setGlobalUpdateState(true)
        cached.clear()
        cachedAlbumArtist.clear()
    }

    private fun setGlobalUpdateState(to: Boolean) {
        isUpdating = to
        musicPlayer.groove.albumArtist.isUpdating = to
        musicPlayer.groove.genre.isUpdating = to
    }
}