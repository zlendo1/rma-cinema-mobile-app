package ba.unsa.etf.gamespirala.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.domain.GameData
import ba.unsa.etf.gamespirala.R
import ba.unsa.etf.gamespirala.activity.GameDetailsActivity
import ba.unsa.etf.gamespirala.adapter.GameListAdapter
import ba.unsa.etf.gamespirala.domain.Game

class HomeFragment : Fragment() {

    private lateinit var games: RecyclerView
    private lateinit var gamesAdapter: GameListAdapter
    private var gamesList: List<Game> = GameData.getAll()

    private var previousGame: Game? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        games = view.findViewById(R.id.game_list)
        games.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        gamesAdapter = GameListAdapter(arrayListOf()) { game ->
            showGameDetails(game)
        }

        games.adapter = gamesAdapter
        gamesAdapter.updateGames(gamesList)

        if (view.intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            handleSendText(intent)
        }

        val extra = intent.extras
        extra?.let {
            val game = GameData.getDetails(extra.getString("previous_game_title", ""))

            game?.let {
                previousGame = game
            }
        }

        return view
    }

    private fun showGameDetails(game: Game) {
        val intent = Intent(activity, GameDetailsActivity::class.java).apply {
            putExtra("game_title", game.title)
        }

        startActivity(intent)
    }

}