package io.github.larsrosenkilde.musicplayer.services.groove

import android.provider.MediaStore
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.ui.helpers.Assets
import io.github.larsrosenkilde.musicplayer.ui.helpers.createHandyImageRequest
import io.github.larsrosenkilde.musicplayer.utils.*
import java.util.concurrent.ConcurrentHashMap

enum class ArtistSortBy {
    ARTIST_NAME,
    TRACKS_COUNT,
    ALBUMS_COUNT
}

class ArtistRepository(private val musicPlayer: MusicPlayer) {
    private val cached = ConcurrentHashMap<String, Artist>()
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    internal val searcher = FuzzySearcher<Artist>(
        options = listOf(
            FuzzySearchOption({ it.name })
        )
    )

    fun fetch() {
        if (isUpdating) return
        isUpdating = true
        cached.clear()
        onUpdate.dispatch(null)
        val cursor = musicPlayer.applicationContext.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.Artists.ARTIST + " ASC"
        )
        try {
            val updateDispatcher = GrooveRepositoryUpdateDispatcher {
                onUpdate.dispatch(null)
            }
            cursor?.use {
                while (it.moveToNext()) {
                    kotlin
                        .runCatching { Artist.fromCursor(it) }
                        .getOrNull()
                        ?.let { artist ->
                            cached[artist.name] = artist
                            updateDispatcher.increment()
                        }
                }
            }
        } catch (err: Exception) {
            //Logger.error("ArtistRepository", "fetch failed: $err")
        }
        isUpdating = false
        onUpdate.dispatch(null)
    }

    fun getArtistArtworkUri(artistName: String) =
        musicPlayer.groove.album.getAlbumOfArtist(artistName)?.let {
            musicPlayer.groove.album.getAlbumArtworkUri(it.id)
        } ?: musicPlayer.groove.album.getDefaultAlbumArtworkUri()

    fun createArtistArtworkImageRequest(artistName: String) = createHandyImageRequest(
        musicPlayer.applicationContext,
        image = getArtistArtworkUri(artistName),
        fallback = Assets.placeholderId
    )

    fun getAll() = cached.values.toList()
    fun getArtistFromName(artistName: String) = cached[artistName]

    fun search(terms: String) = searcher.search(terms, getAll()).subListNonStrict(7)

    companion object {
        fun sort(artists: List<Artist>, by: ArtistSortBy, reversed: Boolean): List<Artist> {
            val sorted = when (by) {
                ArtistSortBy.ARTIST_NAME -> artists.sortedBy { it.name }
                ArtistSortBy.TRACKS_COUNT -> artists.sortedBy { it.numberOfTracks }
                ArtistSortBy.ALBUMS_COUNT -> artists.sortedBy { it.numberOfAlbums }
            }
            return if (reversed) sorted.reversed() else sorted
        }
    }
}