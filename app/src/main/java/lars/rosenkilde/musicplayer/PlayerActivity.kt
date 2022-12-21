package lars.rosenkilde.musicplayer

import android.os.Bundle
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import lars.rosenkilde.musicplayer.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    private var mMediaPlayer: MediaPlayer? = null
    private val dummySong = R.raw.dummysong
    private var playerState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        title = "Player"
        setContentView(binding.root)

        binding.playButton.setOnClickListener {
            if (!playerState) {
                binding.playButton.isSelected = true
                playMedia()
            } else {
                binding.playButton.isSelected = false
                pauseMedia()
            }
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
        playerState = true
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, dummySong)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    private fun pauseMedia() {
        println("Pausing...")
        playerState = false
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
