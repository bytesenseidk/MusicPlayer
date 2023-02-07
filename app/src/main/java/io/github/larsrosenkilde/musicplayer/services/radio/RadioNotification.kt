package io.github.larsrosenkilde.musicplayer.services.radio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.R

class RadioNotification(private val musicPlayer: MusicPlayer) {
    private val session = MediaSessionCompat(
        musicPlayer.applicationContext,
        MEDIA_SESSION_ID
    )
    private var style =
        androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(session.sessionToken)
    private var builder: NotificationCompat.Builder? = null
    private var manager = RadioNotficationManager(musicPlayer)
    private var receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let { action ->
                handleAction(action)
            }
        }
    }

    fun destroy() {
        cancel()
        session.release()
        musicPlayer.applicationContext.unregisterReceiver(receiver)
    }

    private fun cancel() {
        session.isActive = false
        manager.cancel()
    }

    private var usable = false

    private fun handleAction(action: String) {
        if (!usable) return
        when (action) {
            ACTION_PLAY_PAUSE -> musicPlayer.radio.shorty.playPause()
            ACTION_PREVIOUS -> musicPlayer.radio.shorty.previous()
            ACTION_NEXT -> musicPlayer.radio.shorty.skip()
            ACTION_STOP -> musicPlayer.radio.stop()
        }
    }


    companion object {
        const val CHANNEL_ID = "${R.string.app_name}_media_notification"
        const val NOTIFICATION_ID = 69421
        const val MEDIA_SESSION_ID = "${R.string.app_name}_media_session"

        const val ACTION_PLAY_PAUSE = "${R.string.app_name}_play_pause"
        const val ACTION_PREVIOUS = "${R.string.app_name}_previous"
        const val ACTION_NEXT = "${R.string.app_name}_next"
        const val ACTION_STOP = "${R.string.app_name}_stop"
    }
}