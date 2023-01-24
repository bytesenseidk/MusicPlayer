package io.github.larsrosenkilde.musicplayer.services.groove

import androidx.compose.runtime.Immutable

@Immutable
data class Song(
    val id: Long,
    val title: String,
    val trackNumber: Int?,
    val year: Int?,
    val duration: Long,
    val albumId: Long,
    val albumName: String?,
    val artistId: Long,
    val artistName: String?,
    val composer: String?,
    val additional: AdditionalMetadata,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
    val path: String,
) {
    @Immutable
    data class AdditionalMetadata(
        val albumArtist: String?,
        val genre: String?,
        val bitrate: Int?
    ) {
        companion object {
            fun fromSongCacheAttributes(attributes: SongCache.Attributes) = AdditionalMetadata(
                albumArtist = attributes.albumArtist,
                genre = attributes.genre,
                bitrate = attributes.bitrate
            )
        }
    }
}