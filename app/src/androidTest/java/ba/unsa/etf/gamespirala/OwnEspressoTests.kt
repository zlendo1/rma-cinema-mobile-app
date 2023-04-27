package ba.unsa.etf.gamespirala

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.PositionAssertions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.unsa.etf.gamespirala.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OwnEspressoTests {

    @get:Rule
    var mainRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    /*
    * Testira validnost programa u portrait orijentaciji.
    * Program se pokrece, zatim selektujemo details dugme. Fragment se ne smije mijenjati.
    * Zatim selektujemo stavku, vratimo se na home, pa stisnemo details dugme,
    * koje nas vraca na prethodno odabranu stavku.
    */
    @Test
    fun scenarijTest1() {}

    /*
    * Testira validnost programa u landscape orijentaciji.
    * Program se pokrece, zatim prebacivamo u landscape orijentaciju.
    * Prva prikazana stavka na details fragmentu mora biti prva igrica u GameData.
    * Zatim selektujemo drugu stavku i provjeravamo validnost prelaza.
    */
    @Test
    fun scenarijTest2() {}

    /*
    * Testira da li promjena orijentacije radi pravilno.
    * Zapocinje u portrait, otvrara stavku, pa se vraca na home.
    * Poslje tog, prelazi u landscape i opet vracamo u potrait.
    * Mozemo smatrati ovo kao neku vrstu stress-testa.
    */
    @Test
    fun scenarijTest3() {}

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