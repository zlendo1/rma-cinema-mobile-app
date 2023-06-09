package ba.etf.rma23.projekat.fragment

import android.os.Bundle
import androidx.navigation.NavDirections
import ba.unsa.etf.gamespirala.R

object GameDetailsFragmentDirections {
    fun actionDetailsToHome(gameId: Int): NavDirections =
        object : NavDirections {
            override val actionId: Int
                get() = R.id.action_gameDetails_to_home

            override val arguments: Bundle
                get() {
                    val bundle = Bundle()
                    bundle.putInt("game_id", gameId)

                    return bundle
                }
        }
}