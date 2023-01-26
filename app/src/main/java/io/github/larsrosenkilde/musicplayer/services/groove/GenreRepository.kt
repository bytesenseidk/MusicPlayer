package io.github.larsrosenkilde.musicplayer.services.groove

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer
import io.github.larsrosenkilde.musicplayer.utils.FuzzySearchOption
import io.github.larsrosenkilde.musicplayer.utils.FuzzySearcher
import io.github.larsrosenkilde.musicplayer.utils.subListNonStrict

enum class GenreSortBy {
    GENRE,
    TRACKS_COUNT
}

class GenreRepository(private val musicPlayer: MusicPlayer) {
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    private val searcher = FuzzySearcher<Genre>(
        options = listOf(FuzzySearchOption({ it.name }))
    )

    fun getAll() = musicPlayer.groove.song.cachedGenres.values.toList()

    fun search(terms: String) = searcher.search(terms, getAll()).subListNonStrict(7)

    companion object {
        fun sort(genres: List<Genre>, by: GenreSortBy, reversed: Boolean): List<Genre> {
            val sorted = when (by) {
                GenreSortBy.GENRE -> genres.sortedBy { it.name }
                GenreSortBy.TRACKS_COUNT -> genres.sortedBy { it.numberOfTracks }
            }
            return if (reversed) sorted.reversed() else sorted
        }
    }
}