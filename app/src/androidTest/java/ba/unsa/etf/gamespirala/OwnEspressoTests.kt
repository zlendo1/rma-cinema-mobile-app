package ba.unsa.etf.gamespirala

import android.content.pm.ActivityInfo
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.PositionAssertions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.unsa.etf.gamespirala.activity.MainActivity
import ba.unsa.etf.gamespirala.domain.Game
import ba.unsa.etf.gamespirala.domain.GameData
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.`is` as Is
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OwnEspressoTests {

    private val firstGame = GameData.getAll().first()

    @get:Rule
    var mainRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private fun changeOrientation(orientationId: Int) {
        mainRule.scenario.onActivity {
            it.requestedOrientation = orientationId
        }
    }

    private fun clickOnGame(game: Game) {
        onView(withId(R.id.game_list)).perform(actionOnItem<RecyclerView.ViewHolder>(
            allOf(
                hasDescendant(withText(game.title)),
                hasDescendant(withText(game.releaseDate)),
                hasDescendant(withText(game.rating.toString()))
            ), click()
        ))
    }

    private fun assertFragmentActive(fragmentId: Int) {
        mainRule.scenario.onActivity {
            val wantedFragment = it.supportFragmentManager.findFragmentById(fragmentId)

            assertThat(wantedFragment, Is(notNullValue()))
        }
    }

    private fun assertGameDetailsShown(game: Game) {
        onView(withText(game.title)).check(matches(isDisplayed()))
        onView(withText(game.platform)).check(matches(isDisplayed()))
        onView(withText(game.releaseDate)).check(matches(isDisplayed()))
        onView(withText(game.esrbRating)).check(matches(isDisplayed()))
        onView(withText(game.developer)).check(matches(isDisplayed()))
        onView(withText(game.publisher)).check(matches(isDisplayed()))
        onView(withText(game.genre)).check(matches(isDisplayed()))
        onView(withText(game.description)).check(matches(isDisplayed()))
    }

    private fun clickOnButton(id: Int) {
        onView(withId(id)).perform(click())
    }

    /*
    * Testira validnost programa u portrait orijentaciji.
    * Program se pokrece, zatim selektujemo details dugme. Fragment se ne smije mijenjati.
    * Zatim selektujemo stavku, vratimo se na home, pa stisnemo details dugme,
    * koje nas vraca na prethodno odabranu stavku.
    */
    @Test
    fun scenarijTest1() {
        clickOnButton(R.id.gameDetailsItem)
        assertFragmentActive(R.layout.fragment_home)

        clickOnGame(firstGame)
        assertFragmentActive(R.layout.fragment_game_detail)
        assertGameDetailsShown(firstGame)

        clickOnButton(R.id.homeItem)
        assertFragmentActive(R.layout.fragment_home)

        clickOnButton(R.id.gameDetailsItem)
        assertFragmentActive(R.layout.fragment_game_detail)
        assertFragmentActive(R.layout.fragment_home)
    }

    /*
    * Testira validnost programa u landscape orijentaciji.
    * Program se pokrece, zatim prebacivamo u landscape orijentaciju.
    * Prva prikazana stavka na details fragmentu mora biti prva igrica u GameData.
    * Zatim selektujemo drugu stavku i provjeravamo validnost prelaza.
    */
    @Test
    fun scenarijTest2() {
        changeOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        assertGameDetailsShown(firstGame)

        val secondGame = GameData.getAll()[1]

        clickOnGame(secondGame)
        assertGameDetailsShown(secondGame)
    }

    /*
    * Testira da li promjena orijentacije radi pravilno.
    * Zapocinje u portrait, otvrara stavku, pa se vraca na home.
    * Poslje tog, prelazi u landscape i opet vracamo u potrait.
    * Mozemo smatrati ovo kao neku vrstu stress-testa.
    */
    @Test
    fun scenarijTest3() {
        clickOnGame(firstGame)

        assertFragmentActive(R.layout.fragment_game_detail)
        assertGameDetailsShown(firstGame)

        clickOnButton(R.id.homeItem)
        assertFragmentActive(R.layout.fragment_home)

        changeOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        assertFragmentActive(R.layout.fragment_home)
        assertFragmentActive(R.layout.fragment_game_detail)

        changeOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        assertFragmentActive(R.layout.fragment_home)
    }

    /*
    * Testira pozicije i raspored elemenata u fragment_game_details.
    */
    @Test
    fun gameDetailsLayoutTest() {
        onView(withId(R.id.item_title_textview)).check(isCompletelyAbove(withId(R.id.cover_imageview)))
        onView(withId(R.id.cover_imageview)).check(isCompletelyAbove(withId(R.id.platform_textview)))
        onView(withId(R.id.platform_textview)).check(isCompletelyAbove(withId(R.id.date_textview)))
        onView(withId(R.id.date_textview)).check(isCompletelyAbove(withId(R.id.esrb_rating_textview)))
        onView(withId(R.id.esrb_rating_textview)).check(isCompletelyAbove(withId(R.id.developer_textview)))
        onView(withId(R.id.developer_textview)).check(isCompletelyAbove(withId(R.id.publisher_textview)))
        onView(withId(R.id.publisher_textview)).check(isCompletelyAbove(withId(R.id.genre_textview)))
        onView(withId(R.id.genre_textview)).check(isCompletelyAbove(withId(R.id.description_textview)))
        onView(withId(R.id.description_textview)).check(isCompletelyAbove(withId(R.id.impressions_list)))
    }

}