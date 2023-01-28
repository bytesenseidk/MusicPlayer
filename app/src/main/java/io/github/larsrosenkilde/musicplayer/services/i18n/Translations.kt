package io.github.larsrosenkilde.musicplayer.services.i18n

import io.github.larsrosenkilde.musicplayer.services.i18n.translations.English

interface Translations {
    val language: String
    val songs: String
    val genres: String
    val artists: String
    val albums: String
    val albumArtists: String
    val playlists: String
    val settings: String
    val damnThisIsSoEmpty: String
    val playNext: String
    val nowPlaying: String
    val addToQueue: String
    val viewArtist: String
    val viewAlbumArtist: String
    val viewAlbum: String
    val details: String
    val trackName: String
    val artist: String
    val albumArtist: String
    val composer: String
    val unk: String
    val year: String
    val duration: String
    val genre: String
    val bitrate: String
    val filename: String
    val path: String
    val size: String
    val dateAdded: String
    val lastModified: String

    fun XSongs(x: Int): String
    fun XKbps(x: Int): String

    companion object {
        val all = arrayOf<Translations>(English())
        val default = all.first()

        fun of(language: String) = all.find {
            it.language == language
        }
    }
}