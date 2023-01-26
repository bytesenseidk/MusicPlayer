package io.github.larsrosenkilde.musicplayer.ui.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.services.groove.Artist
import io.github.larsrosenkilde.musicplayer.services.groove.Song
import io.github.larsrosenkilde.musicplayer.utils.swap

class HomeViewData(val musicPlayer: MusicPlayer) {
    val songsIsUpdating by mutableStateOf(musicPlayer.groove.song.isUpdating)
    var songs = mutableStateListOf<Song>().apply {
        swap(musicPlayer.groove.song.getAll())
    }
    var songsExplorerId by mutableStateOf(System.currentTimeMillis())

    var artistsIsUpdating by mutableStateOf(musicPlayer.groove.artist.isUpdating)
    val artists = mutableStateListOf<Artist>().apply {
        swap(musicPlayer.groove.artist.getAll())
    }
}