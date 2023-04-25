package ba.unsa.etf.gamespirala.fragment

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
import ba.unsa.etf.gamespirala.domain.GameData
import ba.unsa.etf.gamespirala.R
import ba.unsa.etf.gamespirala.activity.OrientationChange
import ba.unsa.etf.gamespirala.activity.OrientationChange.onOrientation
import ba.unsa.etf.gamespirala.adapter.GameListAdapter
import ba.unsa.etf.gamespirala.domain.Game
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var games: RecyclerView
    private lateinit var gamesAdapter: GameListAdapter
    private var gamesList: List<Game> = GameData.getAll()

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

        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bottom_nav)
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.gameDetailsFragment -> {
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
                val navHostFagmentDetails = requireActivity().supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_details) as NavHostFragment
                val navControllerDetails = navHostFagmentDetails.navController

                navControllerDetails.navigate(action)
            }
        )
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            searchText.setText(it)
        }
    }

}