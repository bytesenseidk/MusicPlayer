package io.github.larsrosenkilde.musicplayer.services

import android.content.Context
import android.content.SharedPreferences
import io.github.larsrosenkilde.musicplayer.MusicPlayer

object SettingsKeys {
    const val identifier = "settings"
    const val fade_playback = "fade_playback"
    const val fade_playback_duration = "fade_playback_duration"
}

object SettingsDataDefaults {
    const val fadePlayback = false
    const val fadePlaybackDuration = 1f
}

class SettingsManager(private val musicPlayer: MusicPlayer) {

    fun getFadePlayback() =
        getSharedPreference().getBoolean(
            SettingsKeys.fade_playback,
            SettingsDataDefaults.fadePlayback
        )

    fun getFadePlaybackDuration() =
        getSharedPreference().getFloat(
            SettingsKeys.fade_playback_duration,
            SettingsDataDefaults.fadePlaybackDuration
        )

    private fun getSharedPreference() =
        musicPlayer.applicationContext.getSharedPreferences(
            SettingsKeys.identifier,
            Context.MODE_PRIVATE
        )
}

private inline fun <reified T: Enum<T>> SharedPreferences.getEnum(
    key: String,
    defaultValue: T?
): T? {
    var result = defaultValue
    getString(key, null)?.let { value ->
        result = parsedEnumValue<T>(value)
    }
    return result
}

private inline fun <reified T: Enum<T>> SharedPreferences.Editor.putEnum(
    key: String,
    value: T?
) = putString(key, value?.name)

private inline fun <reified T: Enum<T>> parsedEnumValue(value: String): T? =
    T::class.java.enumConstants?.find { it.name == value }