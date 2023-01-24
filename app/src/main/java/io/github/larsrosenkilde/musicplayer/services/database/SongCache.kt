package io.github.larsrosenkilde.musicplayer.services.database

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.services.database.adapters.FileDatabaseAdapter
import io.github.larsrosenkilde.musicplayer.services.groove.Song
import io.github.larsrosenkilde.musicplayer.utils.getIntOrNull
import io.github.larsrosenkilde.musicplayer.utils.getStringOrNull
import org.json.JSONObject
import org.w3c.dom.Attr
import java.nio.file.Paths

class SongCache(val musicPlayer: MusicPlayer) {
    data class Attributes(
        val lastModified: Long,
        val albumArtist: String?,
        val genre: String?,
        val bitrate: Int?
    ) {
        fun toJSONObject() = JSONObject().apply {
            put(LAST_MODIFIED, lastModified)
            put(ALBUM_ARTIST, albumArtist)
            put(GENRE, genre)
            put(BITRATE, bitrate)
        }

        companion object {
            private const val LAST_MODIFIED = "0"
            private const val ALBUM_ARTIST = "1"
            private const val BITRATE = "2"
            private const val GENRE = "3"

            fun fromJSONObject(json: JSONObject) = json.run {
                Attributes(
                    lastModified = getLong(LAST_MODIFIED),
                    albumArtist = getStringOrNull(ALBUM_ARTIST),
                    genre = getStringOrNull(GENRE),
                    bitrate = getIntOrNull(BITRATE)
                )
            }

            fun fromSong(song: Song) = Attributes(
                lastModified = song.dateModified,
                albumArtist = song.additional.albumArtist,
                genre = song.additional.genre,
                bitrate = song.additional.bitrate
            )
        }
    }

    private val adapter = FileDatabaseAdapter(
        Paths
            .get(musicPlayer.applicationContext.cacheDir.absolutePath, "song_cache.json")
            .toFile()
    )

    fun read(): Map<Long, Attributes> {
        val content = adapter.read()
        val output = mutableMapOf<Long, Attributes>()
        val parsed = JSONObject(content)
        for (x in parsed.keys()) {
            output[x.toLong()] = Attributes.fromJSONObject(parsed.getJSONObject(x))
        }
        return output
    }

    fun update(value: Map<Long, Attributes>) {
        val json = JSONObject()
        value.forEach { (k, v) ->
            json.put(k.toString(), v.toJSONObject())
        }
        adapter.overwrite(json.toString())
    }
}