package io.github.larsrosenkilde.musicplayer.services.groove

import android.database.Cursor
import android.provider.MediaStore.Audio.AlbumColumns
import androidx.compose.runtime.Immutable
import androidx.core.database.getStringOrNull
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.getColumnValue
import io.github.larsrosenkilde.musicplayer.utils.getColumnValueNullable

@Immutable
data class Album (
    val id: Long,
    val name: String,
    val artist: String?,
    val numberOfTracks: Int
) {
    fun createArtwirkImageRequest(musicPlayer: MusicPlayer) =
        musicPlayer.groove.album.createAlbumArtworkImageRequest(id)

    companion object {
        fun fromCursor(cursor: Cursor): Album {
            return Album(
                id = cursor.getColumnValue(AlbumColumns.ALBUM_ID) {
                    cursor.getLong(it)
                },
                name = cursor.getColumnValue(AlbumColumns.ALBUM) {
                    cursor.getString(it)
                },
                artist = cursor.getColumnValueNullable(AlbumColumns.ARTIST) {
                    cursor.getStringOrNull(it)
                },
                numberOfTracks = cursor.getColumnValue(AlbumColumns.NUMBER_OF_SONGS) {
                    cursor.getInt(it)
                }
            )
        }
    }
}