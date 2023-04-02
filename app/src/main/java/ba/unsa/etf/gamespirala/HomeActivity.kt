package ba.unsa.etf.gamespirala

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.staticdata.Game

class HomeActivity : AppCompatActivity() {

    private lateinit var games: RecyclerView
    private lateinit var gamesAdapter: GameListAdapter
    private lateinit var searchText: EditText
    private var gamesList: List<Game> = getGames()

    private lateinit var homeButton: Button
    private lateinit var detailsButton: Button

    private var previousGame: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeButton = findViewById(R.id.home_button)
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
            previousGame = game
        }

        games.adapter = gamesAdapter
        gamesAdapter.updateGames(gamesList)

        if (intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            handleSendText(intent)
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