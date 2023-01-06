package lars.rosenkilde.musicplayer

import android.os.Bundle
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
                binding.playButtonBg.background = ContextCompat.getDrawable(this, R.drawable.layout_navbar_neon_green)
                binding.nextButtonBg.background = ContextCompat.getDrawable(this, R.drawable.layout_navbar_neon_green)
                binding.prevButtonBg.background = ContextCompat.getDrawable(this, R.drawable.layout_navbar_neon_green)
                playMedia()
            } else {
                binding.playButton.isSelected = false
                binding.playButtonBg.background = ContextCompat.getDrawable(this, R.drawable.layout_navbar_neon_red)
                binding.nextButtonBg.background = ContextCompat.getDrawable(this, R.drawable.layout_navbar_neon_red)
                binding.prevButtonBg.background = ContextCompat.getDrawable(this, R.drawable.layout_navbar_neon_red)
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

    private fun testFunc() {
        val songname = MetaDataExtractor().testString
        if (!playerState) {
            binding.songTitle.text = songname
        } else {
            binding.songTitle.text = "Paused"
        }

    }

    private fun playMedia() {
        testFunc()
        playerState = true
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, dummySong)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    private fun pauseMedia() {
        testFunc()
        playerState = false
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    private fun stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
}
