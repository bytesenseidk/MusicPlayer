package io.github.larsrosenkilde.musicplayer.ui.view.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.larsrosenkilde.musicplayer.ui.components.SongList
import io.github.larsrosenkilde.musicplayer.ui.helpers.ViewContext
import java.lang.reflect.Modifier

@Composable
fun SongsView(context: ViewContext, data: HomeViewData) {
    when {
        data.songs.isNotEmpty() -> SongList(
            context,
            data.songs,
            isLoading = data.songsIsUpdating
        )
        else -> IconTextBody(
            icon = { modifier ->
                Icon(
                    Icons.Default.MusicNote,
                    null,
                    modifier = modifier
                )
            },
            content = { Text(context.musicPlayer.t.damnThisIsSoEmpty) }
        )
    }
}