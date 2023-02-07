package io.github.larsrosenkilde.musicplayer.ui.view.home

import androidx.compose.runtime.Composable
import io.github.larsrosenkilde.musicplayer.ui.helpers.ViewContext

@Composable
fun AlbumsView(context: ViewContext, data: HomeViewData) {
    when {
        data.albums.isNotEmpty() -> AlbumGrid(

        )
    }
}