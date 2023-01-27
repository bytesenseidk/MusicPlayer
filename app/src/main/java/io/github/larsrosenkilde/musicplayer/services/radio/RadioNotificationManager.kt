package io.github.larsrosenkilde.musicplayer.services.radio

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer

class RadioNotficationManager(musicPlayer: MusicPlayer) {
    private var manager = NotificationManagerCompat.from(musicPlayer.applicationContext)
    private var lastNotification: Notification? = null

    enum class State {
        PREPARING,
        READY,
        DESTROYED
    }
    private var state = State.DESTROYED
    private val service: RadioNotificationService?
        get() = RadioNotificationService.instance
    private val hasService: Boolean
        get() = state == State.READY && service != null


}

enum class RadioNotificationServiceEvents {
    START,
    STOP
}
class RadioNotificationService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        instance = this
        events.dispatch(RadioNotificationServiceEvents.START)
        return START_NOT_STICKY
    }

    companion object {
        val events = Eventer<RadioNotificationServiceEvents>()
        var instance: RadioNotificationService? = null

        fun destroy() {
            instance?.let {
                instance = null
                it.stopForeground(STOP_FOREGROUND_REMOVE)
                it.stopSelf()
                events.dispatch(RadioNotificationServiceEvents.STOP)
            }
        }
    }
}