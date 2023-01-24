package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer
import io.github.larsrosenkilde.musicplayer.utils.FuzzySearchOption
import io.github.larsrosenkilde.musicplayer.utils.FuzzySearcher
import java.util.concurrent.ConcurrentHashMap

class ArtistRepository(private val musicPlayer: MusicPlayer) {
    private val cached = ConcurrentHashMap<String, Artist>()
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    internal val searcher = FuzzySearcher<Artist>(
        options = listOf(
            FuzzySearchOption({ it.name })
        )
    )
    fun getArtistFromName(artistName: String) = cached[artistName]
}