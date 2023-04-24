package ba.unsa.etf.gamespirala.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.domain.GameData
import ba.unsa.etf.gamespirala.R
import ba.unsa.etf.gamespirala.adapter.GameListAdapter
import ba.unsa.etf.gamespirala.domain.Game
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var games: RecyclerView
    private lateinit var gamesAdapter: GameListAdapter
    private var gamesList: List<Game> = GameData.getAll()

    private var previousGame: Game? = null

    private var bottomNav: BottomNavigationView? = null
    private lateinit var searchText: EditText

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        navController = findNavController()

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

        val intent: Intent? = activity?.intent
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            handleSendText(intent)
        }

        arguments?.let {
            val gameTitle = it.getString("game_title", "")
            val game = GameData.getDetails(gameTitle)

            game?.let {
                previousGame = game
            }
        }

        bottomNav = activity?.findViewById(R.id.bottom_nav)
        bottomNav?.isEnabled = (previousGame != null)
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home_to_gameDetails -> {
                    previousGame?.let {
                        showGameDetails(it)
                    }

                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false
            }
        }

        return view
    }

    private fun showGameDetails(game: Game) {
        val action = HomeFragmentDirections.actionHomeToDetails(game.title)

        navController.navigate(action)
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            searchText.setText(it)
        }
    }

}