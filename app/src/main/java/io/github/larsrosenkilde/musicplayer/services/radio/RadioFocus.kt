package io.github.larsrosenkilde.musicplayer.services.radio

import android.media.AudioManager
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat
import io.github.larsrosenkilde.musicplayer.MusicPlayer

class RadioFocus(val musicPlayer: MusicPlayer) {
    private val audioManager: AudioManager = musicPlayer.applicationContext.getSystemService(AudioManager::class.java)
    private val audioFocusRequest: AudioFocusRequestCompat = AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
        .setAudioAttributes(
            AudioAttributesCompat.Builder()
                .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
                .build()
        )
        .setOnAudioFocusChangeListener { event ->
            when (event) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    when {
                        musicPlayer.radio.isPlaying -> musicPlayer.radio.restoreVolume()
                        else -> musicPlayer.radio.resume()
                    }
                }
                AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    if (!musicPlayer.settings.getIgnoreAudioFocusLoss()) {
                        musicPlayer.radio.pause()
                    }
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    if (musicPlayer.radio.isPlaying) {
                        musicPlayer.radio.duck()
                    }
                }
            }
        }
        .build()

    fun requestFocus() = AudioManagerCompat.requestAudioFocus(
        audioManager,
        audioFocusRequest
    ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED

    fun abandonFocus() =
        AudioManagerCompat.abandonAudioFocusRequest(
            audioManager,
            audioFocusRequest
        ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
}