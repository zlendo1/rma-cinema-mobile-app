package ba.unsa.etf.gamespirala.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.domain.GameData
import ba.unsa.etf.gamespirala.R
import ba.unsa.etf.gamespirala.adapter.GameListAdapter
import ba.unsa.etf.gamespirala.domain.Game

class HomeActivity : AppCompatActivity() {

    private lateinit var games: RecyclerView
    private lateinit var gamesAdapter: GameListAdapter
    private lateinit var searchText: EditText
    private var gamesList: List<Game> = GameData.getAll()

    private lateinit var detailsButton: Button

    private var previousGame: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        detailsButton = findViewById(R.id.details_button)
        detailsButton.setOnClickListener {
            previousGame?.let { showGameDetails(it) }
        }

        games = findViewById(R.id.game_list)
        games.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        gamesAdapter = GameListAdapter(arrayListOf()) { game ->
            showGameDetails(game)
        }

        games.adapter = gamesAdapter
        gamesAdapter.updateGames(gamesList)

        if (intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            handleSendText(intent)
        }

        val extra = intent.extras
        extra?.let {
            val game = GameData.getDetails(extra.getString("previous_game_title", ""))

            game?.let {
                previousGame = game
            }
        }
    }

    private fun showGameDetails(game: Game) {
        val intent = Intent(this, GameDetailsActivity::class.java).apply {
            putExtra("game_title", game.title)
        }

        startActivity(intent)
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            searchText.setText(it)
        }
    }

}