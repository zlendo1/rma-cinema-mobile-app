package ba.etf.rma23.projekat.fragment

import android.os.Bundle
import androidx.navigation.NavDirections
import ba.unsa.etf.gamespirala.R

object HomeFragmentDirections {
    fun actionHomeToDetails(gameId: Int): NavDirections =
        object : NavDirections {
            override val actionId: Int
                get() = R.id.action_home_to_gameDetails

            override val arguments: Bundle
                get() {
                    val bundle = Bundle()
                    bundle.putInt("game_id", gameId)

                    return bundle
                }
        }
}