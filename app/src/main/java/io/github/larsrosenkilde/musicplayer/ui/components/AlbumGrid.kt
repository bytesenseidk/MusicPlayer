package io.github.larsrosenkilde.musicplayer.ui.components

import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import io.github.larsrosenkilde.musicplayer.services.groove.Album
import io.github.larsrosenkilde.musicplayer.services.groove.AlbumSortBy
import io.github.larsrosenkilde.musicplayer.ui.helpers.ViewContext

@Composable
fun AlbumGrid(
    context: ViewContext,
    albums: List<Album>, 
    isLoading: Boolean = false
) {
    var sortBy by remember {
        mutableStateOf(
            context.musicPlayer.settings.getLastUsedAlbumsSortBy() ?: AlbumSortBy.ALBUM_NAME
        )
    }
}