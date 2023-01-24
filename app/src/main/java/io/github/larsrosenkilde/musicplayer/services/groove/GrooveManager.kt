package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicHooks
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.services.PermissionEvents
import kotlinx.coroutines.*

enum class GrooveKinds {
    SONG,
    ALBUM,
    ARTIST,
    ALBUM_ARTIST,
    GENRE,
    PLAYLIST
}

class GrooveManager(private val musicPlayer: MusicPlayer): MusicHooks {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val readyDeferred = CompletableDeferred<Boolean>()

    val song = SongRepository(musicPlayer)
    val album = AlbumRepository(musicPlayer)
    val artist = ArtistRepository(musicPlayer)
    val albumArtist = AlbumArtistRepository(musicPlayer)
    val genre = GenreRepository(musicPlayer)
    val playlist = PlaylistRepository(musicPlayer)

    init {
        musicPlayer.permission.onUpdate.subscribe {
            when (it) {
                PermissionEvents.MEDIA_PERMISSION_GRANTED -> fetch()
            }
        }
    }
    private fun fetch(postFetch: () -> Unit = {}) {
        coroutineScope.launch {
            awaitAll(
                async { song.fetch() },
                async { album.fetch() },
                async { artist.fetch() }
            )
            playlist.fetch()
            postFetch()
        }
    }
}