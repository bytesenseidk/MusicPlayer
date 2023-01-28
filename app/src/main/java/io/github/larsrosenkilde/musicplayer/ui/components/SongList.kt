package io.github.larsrosenkilde.musicplayer.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

            )
        }
    }
}