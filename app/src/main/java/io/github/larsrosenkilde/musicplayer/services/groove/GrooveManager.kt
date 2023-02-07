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

class GrooveRepositoryUpdateDispatcher(
    val macCount: Int = 30,
    val minTimeDiff: Int = 200,
    val dispatch: () -> Unit
) {
    var count = 0
    var time = currentTime

    fun increment() {
        if (count > macCount && (currentTime - time) > minTimeDiff) {
            dispatch()
            count = 0
            time = System.currentTimeMillis()
            return
        }
        count++
    }

    companion object {
        private val currentTime: Long get() = System.currentTimeMillis()
    }
}