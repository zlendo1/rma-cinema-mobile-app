package ba.unsa.etf.gamespirala.fragment

import android.os.Bundle
import androidx.navigation.NavDirections
import ba.unsa.etf.gamespirala.R

object GameDetailsFragmentDirections {
    fun actionDetailsToHome(data: String): NavDirections =
        object : NavDirections {
            override val actionId: Int
                get() = R.id.action_gameDetails_to_home

            override val arguments: Bundle
                get() {
                    val bundle = Bundle()
                    bundle.putString("game_title", data)

                    return bundle
                }
        }
}