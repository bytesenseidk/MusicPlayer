package lars.rosenkilde.musicplayer

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import lars.rosenkilde.musicplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var intentPlayer: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        title = "Player"
        intentPlayer = Intent(this, PlayerActivity::class.java)
        setContentView(binding.root)
        startActivity(intentPlayer)
    }
}
