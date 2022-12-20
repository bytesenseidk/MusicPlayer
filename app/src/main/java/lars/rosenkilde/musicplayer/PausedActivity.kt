package lars.rosenkilde.musicplayer

import android.os.Bundle
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import lars.rosenkilde.musicplayer.databinding.ActivityPlayerStoppedBinding


class PausedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerStoppedBinding
    private lateinit var intentPlaying: Intent
    private lateinit var intentPaused: Intent

    private var mMediaPlayer: MediaPlayer? = null
    private val dummySong = R.raw.dummysong

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerStoppedBinding.inflate(layoutInflater)
        title = "Player"
        intentPlaying = Intent(this, PlayingActivity::class.java)
        intentPaused = Intent(this, PausedActivity::class.java)
        setContentView(binding.root)

        pauseMedia()

        binding.playButton.setOnClickListener {
            startActivity(intentPlaying)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    private fun playMedia() {
        println("Playing...")
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, dummySong)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    private fun pauseMedia() {
        println("Pausing...")
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    private fun stop() {
        println("Stopping...")
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
}
