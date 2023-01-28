package io.github.larsrosenkilde.musicplayer.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.larsrosenkilde.musicplayer.services.groove.GrooveKinds
import io.github.larsrosenkilde.musicplayer.services.groove.Song
import io.github.larsrosenkilde.musicplayer.services.groove.SongRepository
import io.github.larsrosenkilde.musicplayer.services.groove.SongSortBy
import io.github.larsrosenkilde.musicplayer.ui.helpers.ViewContext

@Composable
fun SongList(
    context: ViewContext,
    songs: List<Song>,
    leadingContent: (LazyListScope.() -> Unit)? = null,
    trailingContent: (LazyListScope.() -> Unit)? = null,
    isLoading: Boolean = false
) {
    var sortBy by remember {
        mutableStateOf(
            context.musicPlayer.settings.getLastUsedSongsSortBy() ?: SongSortBy.TITLE
        )
    }
    var sortReverse by remember {
        mutableStateOf(context.musicPlayer.settings.getLastUsedSongsSortReverse())
    }
    val sortedSongs by remember {
        derivedStateOf { SongRepository.sort(songs, sortBy, sortReverse) }
    }

    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.drawScrollBar(lazyListState)
    ) {
        leadingContent?.invoke(this)
        item {
            MediaSortBar(
                context,
                reverse = sortReverse,
                onReverseChange = {
                    sortReverse = it
                    context.musicPlayer.settings.setLastUsedSongsSortReverse(it)
                },
                sort = sortBy,
                sorts = SongSortBy.values().associateWith { x -> { x.label(it) } },
                onSortChange = {
                    sortBy = it
                    context.musicPlayer.settings.setLastUsedSongsSortBy(it)
                },
                label = {
                    Text(context.musicPlayer.t.XSongs(songs.size))
                },
                isLoading = isLoading,
                onShufflePlay = {
                    context.musicPlayer.radio.shorty.playQueue(sortedSongs, shuffle = true)
                }
            )
        }
        itemsIndexed(
            sortedSongs,
            key = { _, _ -> GrooveKinds.SONG}
        ) { i, song ->
            SongCard(context, song) {
                context.musicPlayer.radio.shorty.playQueue(
                    sortedSongs,
                    Radio.PlayOptions(index = i)
                )
            }
        }
        trailingContent?.invoke(this)
    }
}

private fun SongSortBy.label(context: ViewContext) = when (this) {
    SongSortBy.TITLE -> context.musicPlayer.t.title
    SongSortBy.ARTIST -> context.musicPlayer.t.artist
    SongSortBy.ALBUM -> context.musicPlayer.t.album
    SongSortBy.DURATION -> context.musicPlayer.t.duration
    SongSortBy.DATE_ADDED -> context.musicPlayer.t.dateAdded
    SongSortBy.DATE_MODIFIED -> context.musicPlayer.t.lastModified
    SongSortBy.COMPOSER -> context.musicPlayer.t.composer
    SongSortBy.ALBUM_ARTIST -> context.musicPlayer.t.albumArtist
    SongSortBy.YEAR -> context.musicPlayer.t.year
    SongSortBy.FILENAME -> context.musicPlayer.t.filename
}