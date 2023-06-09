package ba.etf.rma23.projekat

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ba.etf.rma23.projekat.activity.OrientationChange.onOrientation
import ba.etf.rma23.projekat.data.repositories.AccountGamesRepository
import ba.etf.rma23.projekat.domain.GameData
import ba.etf.rma23.projekat.fragment.HomeFragmentDirections
import ba.unsa.etf.gamespirala.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AccountGamesRepository.setAge(18)

        updateOrientationState(resources.configuration)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        updateOrientationState(newConfig)
    }

    private fun updateOrientationState(configuration: Configuration) {
        onOrientation(configuration,
            {
                setContentView(R.layout.activity_main_portrait)

                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController

                val navView: BottomNavigationView = findViewById(R.id.bottom_nav)
                navView.setupWithNavController(navController)
            },
            {
                setContentView(R.layout.activity_main_landspace)

                val navHostFagmentDetails = supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_details) as NavHostFragment
                val navControllerDetails = navHostFagmentDetails.navController
                navControllerDetails.setGraph(R.navigation.navgraph)

                val firstGame = GameData.getAll().first()
                val action = HomeFragmentDirections.actionHomeToDetails(firstGame.id)

                navControllerDetails.navigate(action)
            }
        )
    }

}