package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer
import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap

enum class PlaylistSortBy {
    TITLE,
    TRACKS_COUNT
}

class PlaylistRepository(private val musicPlayer: MusicPlayer) {
    private val cached = ConcurrentHashMap<String, Playlist>()
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    fun fetch() {
        if (isUpdating) return
        isUpdating = true
        try {
            val data = musicPlayer.database.playlist.read()
            data.custom.forEach { cached[it.id] = it }
            data.local.forEach {
                try {
                    val playlist = Playlist.fromM3U(musicPlayer, it)
                    cached[playlist.id] = playlist
                } catch (err: Exception) {
                    //Logger.error("PlaylistRepository", "parsing ${it.path} failed: $err")
                }
            }
        } catch (_: FileNotFoundException) {
        } catch (err: Exception) {
            //Logger.error("PlaylistRepository", "parsing ${it.path} failed: $err")
        }
        isUpdating = false
        onUpdate.dispatch(null)
    }

    fun getAll() = cached.values.toList()
    fun getPlaylistWithId(id: String) = cached[id]
}