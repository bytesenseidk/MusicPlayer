package io.github.larsrosenkilde.musicplayer.ui.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.larsrosenkilde.musicplayer.ui.helpers.*
import io.github.larsrosenkilde.musicplayer.ui.view.home.*
import io.github.larsrosenkilde.musicplayer.services.i18n.*
import io.github.larsrosenkilde.musicplayer.ui.components.NowPlayingBottomBar
import io.github.larsrosenkilde.musicplayer.ui.components.TopAppBarMinimalTitle

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

    LaunchedEffect(LocalContext.current) {
        data.initialize()
    }

    DisposableEffect(LocalContext.current) {
        onDispose { data.dispose() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(Icons.Default.Search, null)
                        },
                        onClick = {
                            context.navController.navigate(Routes.Search)
                        }
                    )
                },
                title = {
                    Crossfade(targetState = currentPage.label(context)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TopAppBarMinimalTitle { Text(it) }
                        }
                    }
                },
                actions = {
                    IconButton(
                        content = {
                            Icon(Icons.Default.MoreVert, null)
                            DropdownMenu(
                                expanded = showOptionsDropdown,
                                onDismissRequest = { showOptionsDropdown = false }
                            ) {
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Settings,
                                            context.musicPlayer.t.settings
                                        )
                                    },
                                    text = {
                                        Text(context.musicPlayer.t.settings)
                                    },
                                    onClick = {
                                        showOptionsDropdown = false
                                        context.navController.navigate(Routes.Settings)
                                    }
                                )
                            }
                        },
                        onClick = {
                            showOptionsDropdown = !showOptionsDropdown
                        }
                    )
                }
            )
        },
        content = { contentPadding ->
            AnimatedContent(
                targetState = currentPage,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                transitionSpec = {
                    SlideTransitions.slideUp.enterTransition()
                        .with(ScaleTransition.scaleDown.exitTransition())
                },
            ) { page ->
                when (page) {
                    HomePages.Songs -> SongsView(context, data)
                    HomePages.Albums -> AlbumsView(context, data)
                    HomePages.Artists -> ArtistsView(context, data)
                    HomePages.AlbumArtists -> AlbumArtistsView(context, data)
                    HomePages.Genres -> GenresView(context, data)
                    HomePages.Playlists -> PlaylistsView(context, data)
                }
            }
        },
        bottomBar = {
            Column {
                NowPlayingBottomBar(context)
                NavigationBar {
                    Spacer(modifier = Modifier.width(2.dp))
                    tabs.map { page ->
                        val isSelected = currentPage == page
                        val label = page.label(context)
                        NavigationBarItem(
                            selected = isSelected,
                            alwaysShowLabel = labelVisibility == HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
                            icon = {
                                Crossfade(targetState == isSelected) {
                                    Icon(
                                        if (it) page.selectedIcon() else page.unselectedIcon(),
                                        label
                                    )
                                }
                            },
                            label = when (labelVisibility) {
                                HomePageBottomBarLabelVisibility.INVISIBLE -> null
                                else -> ({
                                    Text(
                                        label,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center,
                                        overfloq = TextOverflow.Ellipsis,
                                        softWrap = false
                                    )
                                })
                            },
                            onClick = {
                                currentPage = page
                                context.musicPlayer.settings.setHomeLastTab(currentPage)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
    )
}