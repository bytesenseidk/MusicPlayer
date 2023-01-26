package io.github.larsrosenkilde.musicplayer.ui.helpers

import androidx.navigation.NavHostController
import io.github.larsrosenkilde.musicplayer.MainActivity
import io.github.larsrosenkilde.musicplayer.MusicPlayer

data class ViewContext(
    val musicPlayer: MusicPlayer,
    val activity: MainActivity,
    val navController: NavHostController
)