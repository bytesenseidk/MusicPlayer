package io.github.larsrosenkilde.musicplayer.services.groove

import androidx.compose.runtime.Immutable

@Immutable
data class Artist(
    val name: String,
    val numberOfAlbums: Int,
    val numberOfTracks: Int
) {

}