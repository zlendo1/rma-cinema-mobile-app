package ba.unsa.etf.gamespirala.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.domain.GameData
import ba.unsa.etf.gamespirala.R
import ba.unsa.etf.gamespirala.adapter.ImpressionListAdapter
import ba.unsa.etf.gamespirala.domain.Game
import ba.unsa.etf.gamespirala.domain.UserImpression

class GameDetailsActivity: AppCompatActivity() {

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

    private lateinit var homeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        homeButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            showHome()
        }

        title = findViewById(R.id.game_title_textview)
        cover = findViewById(R.id.cover_imageview)
        platform = findViewById(R.id.platform_textview)
        date = findViewById(R.id.date_textview)
        esrbRating = findViewById(R.id.esrb_rating_textview)
        developer = findViewById(R.id.developer_textview)
        publisher = findViewById(R.id.publisher_textview)
        genre = findViewById(R.id.genre_textview)
        description = findViewById(R.id.description_textview)

        impressions = findViewById(R.id.impressions_list)
        impressions.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val extras = intent.extras
        if (extras != null) {
            val game = GameData.getDetails(extras.getString("game_title", ""))

            if (game != null) {
                this.game = game
            }

            populateDetails()
        } else {
            finish()
        }

        impressionsList = game.userImpressions

        impressionsAdapter = ImpressionListAdapter(arrayListOf())

        impressions.adapter = impressionsAdapter
        impressionsAdapter.updateImpressions(impressionsList)
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
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("previous_game_title", game.title)
        }

        startActivity(intent)
    }

}
