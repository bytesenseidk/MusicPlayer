package lars.rosenkilde.musicplayer

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import lars.rosenkilde.musicplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var intentPaused: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        title = "Player"
        intentPaused = Intent(this, PausedActivity::class.java)
        setContentView(binding.root)
        startActivity(intentPaused)
    }
}
