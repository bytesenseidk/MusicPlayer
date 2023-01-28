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

    companion object {
        val all = arrayOf<Translations>(English())
        val default = all.first()

        fun of(language: String) = all.find {
            it.language == language
        }
    }
}