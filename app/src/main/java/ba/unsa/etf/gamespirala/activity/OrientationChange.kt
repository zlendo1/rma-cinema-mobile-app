package ba.unsa.etf.gamespirala.activity

import android.content.res.Configuration

object OrientationChange {

    fun onOrientation(configuration: Configuration, onPortrait: () -> Unit, onLandscape: () -> Unit) {
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            onPortrait()
        } else {
            onLandscape()
        }
    }

}