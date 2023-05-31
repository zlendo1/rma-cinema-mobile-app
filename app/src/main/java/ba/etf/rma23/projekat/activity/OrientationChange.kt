package ba.etf.rma23.projekat.activity

import android.content.res.Configuration

object OrientationChange {

    fun onOrientation(configuration: Configuration, onPortrait: () -> Unit, onLandscape: () -> Unit) {
        when (val orientation = configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> onPortrait()
            Configuration.ORIENTATION_LANDSCAPE -> onLandscape()

            else -> throw IllegalStateException("Orientation configuration not allowed for orientation with ID: $orientation")
        }
    }

}