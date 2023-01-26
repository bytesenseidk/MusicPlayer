package io.github.larsrosenkilde.musicplayer.services

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.ui.view.HomePageBottomBarLabelVisibility
import io.github.larsrosenkilde.musicplayer.ui.view.HomePages
import io.github.larsrosenkilde.musicplayer.utils.Eventer

object SettingsKeys {
    const val identifier = "settings"
    const val fade_playback = "fade_playback"
    const val fade_playback_duration = "fade_playback_duration"
    const val play_on_headphones_connect = "play_on_headphones_connect"
    const val pause_on_headphones_disconnect = "pause_on_headphones_disconnect"
    const val language = "language"
    const val home_tabs = "home_tabs"
    const val home_last_tab = "home_last_tab"
    const val mini_player_extended_controls = "mini_player_extended_controls"
    const val home_page_bottom_bar_label_visibility = "home_page_bottom_bar_label_visibility"
    const val require_audio_focus = "require_audio_focus"
    const val ignore_audiofocus_loss = "ignore_audiofocus_loss"
}

data class SettingsData(
    val playOnHeadphonesConnect: Boolean,
    val pauseOnHeadphonesDisconnect: Boolean,
    val homeTabs: Set<HomePages>,
    val homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility
)

object SettingsDataDefaults {
    const val fadePlayback = false
    const val fadePlaybackDuration = 1f
    const val playOnHeadphonesConnect = false
    const val pauseOnHeadphonesDisconnect = false
    const val miniPlayerExtendedControls = false
    const val requireAudioFocus = true
    const val ignoreAudioFocusLoss = false
    val homeTabs = setOf(
        HomePages.Songs,
        HomePages.Albums,
        HomePages.Artists
    )
    val homePageBottomBarLabelVisibility = HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE
}

class SettingsManager(private val musicPlayer: MusicPlayer) {
    val onChange = Eventer<String>()

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

    fun getPlayOnHeadphonesConnect() =
        getSharedPreference().getBoolean(
            SettingsKeys.play_on_headphones_connect,
            SettingsDataDefaults.playOnHeadphonesConnect
        )

    fun getPauseOnHeadphonesDisconnect() =
        getSharedPreference().getBoolean(
            SettingsKeys.pause_on_headphones_disconnect,
            SettingsDataDefaults.pauseOnHeadphonesDisconnect
        )

    fun getLanguage() = getSharedPreference().getString(SettingsKeys.language, null)

    fun getHomeTabs() = getSharedPreference()
        .getString(SettingsKeys.home_tabs, null)
        ?.split(",")
        ?.mapNotNull { parsedEnumValue<HomePages>(it) }
        ?.toSet()
        ?: SettingsDataDefaults.homeTabs

    fun setHomeTabs(tabs: Set<HomePages>) {
        getSharedPreference().edit {
            putString(SettingsKeys.home_tabs, tabs.joinToString(",") { it.name })
        }
        onChange.dispatch(SettingsKeys.home_tabs)
    }

    fun getHomePageBottomBarLabelVisibility() =
        getSharedPreference()
            .getEnum(SettingsKeys.home_page_bottom_bar_label_visibility, null)
            ?: SettingsDataDefaults.homePageBottomBarLabelVisibility

    fun setHomePageBottomBarLabelVisibility(value: HomePageBottomBarLabelVisibility) {
        getSharedPreference().edit {
            putEnum(SettingsKeys.home_page_bottom_bar_label_visibility, value)
        }
        onChange.dispatch(SettingsKeys.home_page_bottom_bar_label_visibility)
    }

    fun getHomeLastTab() =
        getSharedPreference().getEnum(SettingsKeys.home_last_tab, null) ?: HomePages.Songs

    fun getMiniPlayerExtendedControls() =
        getSharedPreference().getBoolean(
            SettingsKeys.mini_player_extended_controls,
            SettingsDataDefaults.miniPlayerExtendedControls
        )

    fun getRequiredAudioFocus() =
        getSharedPreference().getBoolean(
            SettingsKeys.require_audio_focus,
            SettingsDataDefaults.requireAudioFocus
        )

    fun getIgnoreAudioFocusLoss() =
        getSharedPreference().getBoolean(
            SettingsKeys.ignore_audiofocus_loss,
            SettingsDataDefaults.ignoreAudioFocusLoss
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