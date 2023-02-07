package io.github.larsrosenkilde.musicplayer.services.database

import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.services.database.adapters.FileDatabaseAdapter
import io.github.larsrosenkilde.musicplayer.services.groove.Playlist
import io.github.larsrosenkilde.musicplayer.utils.toList
import org.json.JSONArray
import org.json.JSONObject
import java.nio.file.Paths

class PlaylistBox(val musicPlayer: MusicPlayer) {
    data class Data(
        val custom: List<Playlist>,
        val local: List<Playlist.Local>
    ) {
        fun toJSONObject() = JSONObject().apply {
            put(CUSTOM, JSONArray(custom.map { it.toJSONObject() }))
            put(LOCAL, JSONArray(local.map { it.toJSONObject() }))
        }

        companion object {
            private const val CUSTOM = "0"
            private const val LOCAL = "1"

            fun fromJSONObject(json: JSONObject) = json.run {
                Data(
                    custom = json.getJSONArray(LOCAL)
                        .toList { Playlist.fromJSONObject(getJSONObject(it)) },
                    local = json.getJSONArray(LOCAL)
                        .toList { Playlist.Local.fromJSONObject(getJSONObject(it)) },
                )
            }
        }
    }

    private val adapter = FileDatabaseAdapter(
        Paths
            .get(musicPlayer.applicationContext.dataDir.absolutePath, "playlists.json")
            .toFile()
    )

    fun read(): Data {
        val content = adapter.read()
        return Data.fromJSONObject(JSONObject(content))
    }

    fun update(value: Data) {
        adapter.overwrite(value.toJSONObject().toString())
    }
}