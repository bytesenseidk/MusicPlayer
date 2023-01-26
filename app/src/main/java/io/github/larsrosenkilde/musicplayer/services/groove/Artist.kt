package io.github.larsrosenkilde.musicplayer.services.groove

import android.database.Cursor
import android.provider.MediaStore.Audio.ArtistColumns
import androidx.compose.runtime.Immutable
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.getColumnValue

@Immutable
data class Artist(
    val name: String,
    val numberOfAlbums: Int,
    val numberOfTracks: Int
) {
    fun createArtworkRequest(musicPlayer: MusicPlayer) =
        musicPlayer.groove.artist.createArtistArtworkImageRequest(name)

    companion object {
        fun fromCursor(cursor: Cursor): Artist {
            return Artist(
                name = cursor.getColumnValue(ArtistColumns.ARTIST) {
                    cursor.getString(it)
                },
                numberOfAlbums = cursor.getColumnValue(ArtistColumns.NUMBER_OF_ALBUMS) {
                    cursor.getInt(it)
                },
                numberOfTracks = cursor.getColumnValue(ArtistColumns.NUMBER_OF_TRACKS) {
                    cursor.getInt(it)
                }
            )
        }
    }
}