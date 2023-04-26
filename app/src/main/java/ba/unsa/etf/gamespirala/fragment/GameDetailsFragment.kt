package ba.unsa.etf.gamespirala.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.R
import ba.unsa.etf.gamespirala.activity.OrientationChange.onOrientation
import ba.unsa.etf.gamespirala.adapter.ImpressionListAdapter
import ba.unsa.etf.gamespirala.domain.Game
import ba.unsa.etf.gamespirala.domain.GameData
import ba.unsa.etf.gamespirala.domain.UserImpression
import com.google.android.material.bottomnavigation.BottomNavigationView

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

    private lateinit var impressions: RecyclerView
    private lateinit var impressionsAdapter: ImpressionListAdapter
    private lateinit var impressionsList: List<UserImpression>

    private lateinit var navController: NavController
    private lateinit var configuration: Configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val gameTitle = it.getString("game_title", "")
            val game = GameData.getDetails(gameTitle)

            if (game != null) {
                this.game = game
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
                R.id.homeFragment -> {
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
        val action = GameDetailsFragmentDirections.actionDetailsToHome(game.title)

        onOrientation(configuration,
            {
                navController.navigate(action)
            },
            {}
        )
    }
}