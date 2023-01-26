package io.github.larsrosenkilde.musicplayer.services.i18n

import io.github.larsrosenkilde.musicplayer.MusicPlayer

class Translator(private val musicPlayer: MusicPlayer) {
    var t: Translations

    init {
        t = musicPlayer.settings.getLanguage()?.let { Translations.of(it) } ?: Translations.default
    }
}