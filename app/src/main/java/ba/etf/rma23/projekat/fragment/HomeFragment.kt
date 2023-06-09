package ba.etf.rma23.projekat.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma23.projekat.domain.GameData
import ba.unsa.etf.gamespirala.R
import ba.etf.rma23.projekat.activity.OrientationChange.onOrientation
import ba.etf.rma23.projekat.adapter.GameListAdapter
import ba.etf.rma23.projekat.data.repositories.AccountGamesRepository
import ba.etf.rma23.projekat.data.repositories.GamesRepository
import ba.etf.rma23.projekat.domain.Game
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var games: RecyclerView
    private lateinit var gamesAdapter: GameListAdapter
    private lateinit var gamesList: List<Game>

    private var previousGame: Game? = null

    private lateinit var searchText: EditText

    private lateinit var navController: NavController
    private lateinit var configuration: Configuration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        navController = findNavController()
        configuration = resources.configuration

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
        getGames("")

        searchText = view.findViewById(R.id.search_query_edittext)

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

        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bottom_nav)
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.gameDetailsItem -> {
                    previousGame?.let {
                        showGameDetails(it)
                    } ?: run {
                        return@setOnItemSelectedListener false
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

        onOrientation(configuration,
            {
                navController.navigate(action)
            },
            {
                previousGame = game

                val navHostFagmentDetails = requireActivity().supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_details) as NavHostFragment
                val navControllerDetails = navHostFagmentDetails.navController
                navControllerDetails.setGraph(R.navigation.navgraph)

                navControllerDetails.navigate(action)
            }
        )
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            searchText.setText(it)

            getGames(it)
        }
    }

    private fun getGames(query: String) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            gamesList = if (query.isEmpty()) {
                AccountGamesRepository.getSavedGames()
            } else {
                GamesRepository.getGamesByName(query)
            }

            gamesAdapter.updateGames(gamesList)
        }
    }

}