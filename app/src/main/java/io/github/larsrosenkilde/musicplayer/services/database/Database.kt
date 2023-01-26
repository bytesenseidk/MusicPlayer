package io.github.larsrosenkilde.musicplayer.services.database

import io.github.larsrosenkilde.musicplayer.MusicPlayer

class Database(musicPlayer: MusicPlayer) {
    val songCache = SongCache(musicPlayer)
    val playlist = PlaylistBox(musicPlayer)
}