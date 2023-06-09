package ba.etf.rma23.projekat.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.R
import ba.etf.rma23.projekat.activity.OrientationChange.onOrientation
import ba.etf.rma23.projekat.adapter.ImpressionListAdapter
import ba.etf.rma23.projekat.auxiliary.getGameById
import ba.etf.rma23.projekat.data.repositories.AccountGamesRepository
import ba.etf.rma23.projekat.domain.Game
import ba.etf.rma23.projekat.domain.UserImpression
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class GameDetailsFragment : Fragment() {

    private lateinit var game: Game

    private lateinit var title: TextView
    private lateinit var cover: ImageView
    private lateinit var platform: TextView
    private lateinit var date: TextView
    private lateinit var esrbRating: TextView
    private lateinit var developer: TextView
    private lateinit var publisher: TextView
    private lateinit var genre: TextView
    private lateinit var description: TextView

    private lateinit var favoriteSwitch: Switch

    private lateinit var impressions: RecyclerView
    private lateinit var impressionsAdapter: ImpressionListAdapter
    private lateinit var impressionsList: List<UserImpression>

    private lateinit var navController: NavController
    private lateinit var configuration: Configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val gameId = it.getInt("game_id", -1)
            var game: Game?

            runBlocking {
                game = getGameById(gameId)
            }

            game?.let {
                this.game = game as Game
            }
        } ?: run {
            throw IllegalArgumentException("No arguments provided")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_detail, container, false)

        navController = findNavController()
        configuration = resources.configuration

        title = view.findViewById(R.id.item_title_textview)
        cover = view.findViewById(R.id.cover_imageview)
        platform = view.findViewById(R.id.platform_textview)
        date = view.findViewById(R.id.date_textview)
        esrbRating = view.findViewById(R.id.esrb_rating_textview)
        developer = view.findViewById(R.id.developer_textview)
        publisher = view.findViewById(R.id.publisher_textview)
        genre = view.findViewById(R.id.genre_textview)
        description = view.findViewById(R.id.description_textview)

        favoriteSwitch = view.findViewById(R.id.favorite_switch)
        favoriteSwitch.isChecked = isFavorite(game)

        favoriteSwitch.setOnClickListener {
            favoriteGameSet()
        }

        impressions = view.findViewById(R.id.impressions_list)
        impressions.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )

        impressionsList = game.userImpressions

        impressionsAdapter = ImpressionListAdapter(arrayListOf())

        impressions.adapter = impressionsAdapter
        impressionsAdapter.updateImpressions(impressionsList)

        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bottom_nav)
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeItem -> {
                    showHome()

                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false
            }
        }

        populateDetails()

        return view
    }

    private fun populateDetails() {
        title.text = game.title
        platform.text = game.platform
        date.text = game.releaseDate
        esrbRating.text = game.esrbRating
        developer.text = game.developer
        publisher.text = game.publisher
        genre.text = game.genre
        description.text = game.description

        val context: Context = cover.context
        val id = context
            .resources
            .getIdentifier(game.coverImage, "mipmap", context.packageName)
        cover.setImageResource(id)
    }

    private fun showHome() {
        val action = GameDetailsFragmentDirections.actionDetailsToHome(game.id)

        onOrientation(configuration,
            {
                navController.navigate(action)
            },
            {}
        )
    }

    private fun isFavorite(game: Game): Boolean {
        var result: Boolean

        runBlocking {
            result = AccountGamesRepository.getGameById(game.id) != null
        }

        return result
    }

    private fun favoriteGameSet() {
        runBlocking {
            if (favoriteSwitch.isChecked) {
                if (!AccountGamesRepository.removeGame(game.id)) {
                    throw Exception("Game not properly removed at details switch")
                }

                favoriteSwitch.isChecked = false
            } else {
                if (AccountGamesRepository.saveGame(game) == null) {
                    throw Exception("Game not properly added at details switch")
                }

                favoriteSwitch.isChecked = true
            }
        }
    }
}