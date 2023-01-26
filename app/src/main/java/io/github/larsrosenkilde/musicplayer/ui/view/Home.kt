package io.github.larsrosenkilde.musicplayer.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import io.github.larsrosenkilde.musicplayer.ui.helpers.*
import io.github.larsrosenkilde.musicplayer.ui.view.home.*
import io.github.larsrosenkilde.musicplayer.services.i18n.*

enum class HomePages(
    val label: (context: ViewContext) -> String,
    val selectedIcon: @Composable () -> ImageVector,
    val unselectedIcon: @Composable () -> ImageVector
) {
    Songs(
        label = { it.musicPlayer.t.songs },
        selectedIcon = { Icons.Filled.MusicNote },
        unselectedIcon = { Icons.Outlined.MusicNote }
    ),
    Artists(
        label = { it.musicPlayer.t.artists },
        selectedIcon = { Icons.Filled.Group },
        unselectedIcon = { Icons.Outlined.Face }
    ),
    Albums(
        label = { it.musicPlayer.t.albums },
        selectedIcon = { Icons.Filled.Album },
        unselectedIcon = { Icons.Outlined.Album }
    ),
    AlbumArtists(
        label = { it.musicPlayer.t.albumArtists },
        selectedIcon = { Icons.Filled.SupervisorAccount },
        unselectedIcon = { Icons.Outlined.SupervisorAccount }
    ),
    Genres(
        label = { it.musicPlayer.t.genres },
        selectedIcon = { Icons.Filled.Tune },
        unselectedIcon = { Icons.Outlined.Tune }
    ),
    Playlists(
        label = { it.musicPlayer.t.playlists },
        selectedIcon = { Icons.Filled.QueueMusic },
        unselectedIcon = { Icons.Outlined.QueueMusic }
    )
}

enum class HomePageBottomBarLabelVisibility {
    ALWAYS_VISIBLE,
    VISIBLE_WHEN_ACTIVE,
    INVISIBLE
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeView(context: ViewContext) {
    val tabs = context.musicPlayer.settings.getHomeTabs().toList()
    val labelVisibility = context.musicPlayer.settings.getHomePageBottomBarLabelVisibility()
    var currentPage by remember {
        mutableStateOf(context.musicPlayer.settings.getHomeLastTab())
    }
    var showOptionsDropdown by remember { mutableStateOf(false) }
    val data = remember { HomeViewData(context.musicPlayer) }
}