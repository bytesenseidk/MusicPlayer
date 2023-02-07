package io.github.larsrosenkilde.musicplayer.ui.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.services.groove.*
import io.github.larsrosenkilde.musicplayer.utils.EventUnsubscribeFn
import io.github.larsrosenkilde.musicplayer.utils.swap

class HomeViewData(val musicPlayer: MusicPlayer) {
    var songsIsUpdating by mutableStateOf(musicPlayer.groove.song.isUpdating)
    val songs = mutableStateListOf<Song>().apply {
        swap(musicPlayer.groove.song.getAll())
    }
    var songsExplorerId by mutableStateOf(System.currentTimeMillis())

    var artistsIsUpdating by mutableStateOf(musicPlayer.groove.artist.isUpdating)
    val artists = mutableStateListOf<Artist>().apply {
        swap(musicPlayer.groove.artist.getAll())
    }

    var albumsIsUpdating by mutableStateOf(musicPlayer.groove.album.isUpdating)
    val albums = mutableStateListOf<Album>().apply {
        swap(musicPlayer.groove.album.getAll())
    }

    var albumArtistsIsUpdating by mutableStateOf(musicPlayer.groove.albumArtist.isUpdating)
    val albumArtists = mutableStateListOf<Artist>().apply {
        swap(musicPlayer.groove.albumArtist.getAll())
    }

    var genresIsUpdating by mutableStateOf(musicPlayer.groove.genre.isUpdating)
    val genres = mutableStateListOf<Genre>().apply {
        swap(musicPlayer.groove.genre.getAll())
    }

    var playlistsIsUpdating by mutableStateOf(musicPlayer.groove.playlist.isUpdating)
    val playlists = mutableStateListOf<Playlist>().apply {
        swap(musicPlayer.groove.playlist.getAll())
    }

    private var songsSubscriber: EventUnsubscribeFn? = null
    private var artistsSubscriber: EventUnsubscribeFn? = null
    private var albumsSubscriber: EventUnsubscribeFn? = null
    private var albumArtistsSubscriber: EventUnsubscribeFn? = null
    private var genresSubscriber: EventUnsubscribeFn? = null
    private var playlistsSubscriber: EventUnsubscribeFn? = null

    fun initialize() {
        updateAllStates()
        songsSubscriber = musicPlayer.groove.song.onUpdate.subscribe { updateSongsState() }
        artistsSubscriber = musicPlayer.groove.artist.onUpdate.subscribe { updateArtistsState() }
        albumsSubscriber = musicPlayer.groove.album.onUpdate.subscribe { updateAlbumsState() }
        albumArtistsSubscriber = musicPlayer.groove.albumArtist.onUpdate.subscribe { updateAlbumArtistsState() }
        genresSubscriber = musicPlayer.groove.genre.onUpdate.subscribe { updateGenresState() }
        playlistsSubscriber = musicPlayer.groove.playlist.onUpdate.subscribe { updatePlaylistsState() }
    }

    fun dispose() {
        songsSubscriber?.invoke()
        artistsSubscriber?.invoke()
        albumsSubscriber?.invoke()
        albumArtistsSubscriber?.invoke()
        genresSubscriber?.invoke()
        playlistsSubscriber?.invoke()
    }

    private fun updateAllStates() {
        updateSongsState()
        updateArtistsState()
        updateAlbumsState()
        updateAlbumArtistsState()
        updateGenresState()
    }

    private fun updateSongsState() {
        songsIsUpdating = musicPlayer.groove.song.isUpdating
        songs.swap(musicPlayer.groove.song.getAll())
        songsExplorerId = System.currentTimeMillis()
    }

    private fun updateArtistsState() {
        artistsIsUpdating = musicPlayer.groove.artist.isUpdating
        artists.swap(musicPlayer.groove.artist.getAll())
    }

    private fun updateAlbumsState() {
        albumsIsUpdating = musicPlayer.groove.album.isUpdating
        albums.swap(musicPlayer.groove.album.getAll())
    }

    private fun updateAlbumArtistsState() {
        albumArtistsIsUpdating = musicPlayer.groove.albumArtist.isUpdating
        albumArtists.swap(musicPlayer.groove.albumArtist.getAll())
    }

    private fun updateGenresState() {
        genresIsUpdating = musicPlayer.groove.genre.isUpdating
        genres.swap(musicPlayer.groove.genre.getAll())
    }

    private fun updatePlaylistsState() {
        playlistsIsUpdating = musicPlayer.groove.playlist.isUpdating
        playlists.swap(musicPlayer.groove.playlist.getAll())
    }
}