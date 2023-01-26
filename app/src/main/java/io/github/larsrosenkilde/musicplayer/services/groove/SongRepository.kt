package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer
import java.util.concurrent.ConcurrentHashMap

enum class SongSortBy {
    TITLE,
    ARTIST,
    ALBUM,
    DURATION,
    DATE_ADDED,
    DATE_MODIFIED,
    COMPOSER,
    ALBUM_ARTIST,
    YEAR,
    FILENAME
}

class SongRepository(private val musicPlayer: MusicPlayer) {
    private val cached = ConcurrentHashMap<Long, Song>()
    private val cachedAlbumArtist = ConcurrentHashMap<String, MutableSet<Long>>()
    internal var cachedGenres = ConcurrentHashMap<String, Genre>()
    internal var cachedPaths = ConcurrentHashMap<String, Long>()
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()
    val explorer = createNewExplorer()

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

    fun getAll() = cached.values.toList()
    private fun getAll(filter: (Song) -> Boolean) = getAll().filter(filter)

    companion object {
        fun sort(songs: List<Song>, by: SongSortBy, reversed: Boolean): List<Song> {
            val sorted = when(by) {
                SongSortBy.TITLE -> songs.sortedBy { it.title }
                SongSortBy.ARTIST -> songs.sortedBy { it.title }
                SongSortBy.ALBUM -> songs.sortedBy { it.title }
                SongSortBy.DURATION -> songs.sortedBy { it.title }
                SongSortBy.DATE_ADDED -> songs.sortedBy { it.title }
                SongSortBy.DATE_MODIFIED -> songs.sortedBy { it.title }
                SongSortBy.COMPOSER -> songs.sortedBy { it.title }
                SongSortBy.ALBUM_ARTIST -> songs.sortedBy { it.title }
                SongSortBy.YEAR -> songs.sortedBy { it.title }
                SongSortBy.FILENAME -> songs.sortedBy { it.title }
            }
            return if (reversed) sorted.reversed() else sorted
        }

        fun createNewExplorer() = GrooveExplorer.Folder("root")
    }
}