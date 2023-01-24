package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer

class AlbumArtistRepository(private val musicPlayer: MusicPlayer) {
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    fun getAll() = musicPlayer.groove.song.getAlbumArtistNames().mapNotNull { artist ->
        musicPlayer.groove.artist.getArtistFromName(artist)
    }

    fun search(terms: String) {
        musicPlayer.groove.artist.searcher.search(terms, getAll()).subListNonStrict(7)
    }
}