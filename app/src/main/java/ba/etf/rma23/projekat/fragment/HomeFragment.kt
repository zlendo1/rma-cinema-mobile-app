package ba.etf.rma23.projekat.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.R
import ba.etf.rma23.projekat.activity.OrientationChange.onOrientation
import ba.etf.rma23.projekat.adapter.GameListAdapter
import ba.etf.rma23.projekat.auxiliary.getGameById
import ba.etf.rma23.projekat.data.repositories.AccountGamesRepository
import ba.etf.rma23.projekat.data.repositories.GamesRepository
import ba.etf.rma23.projekat.domain.Game
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private lateinit var games: RecyclerView
    private lateinit var gamesAdapter: GameListAdapter
    private lateinit var gamesList: List<Game>

    private var previousGame: Game? = null

    private lateinit var searchText: EditText
    private lateinit var searchButton: ImageButton

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

        searchButton = view.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            getGames(searchText.text.toString())
        }

        arguments?.let {
            val gameId = it.getInt("game_id", -1)
            var game: Game?

            runBlocking {
                game = getGameById(gameId)
            }

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
        val action = HomeFragmentDirections.actionHomeToDetails(game.id)

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

    private fun getGames(query: String) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            try {
                gamesList = if (query.isEmpty()) {
                    AccountGamesRepository.getSavedGames()
                } else {
                    GamesRepository.getGamesByName(query)
                }

                gamesAdapter.updateGames(gamesList)
            } catch (_: Exception) {}
        }
    }

}