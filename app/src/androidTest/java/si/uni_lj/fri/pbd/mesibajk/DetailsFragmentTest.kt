package si.uni_lj.fri.pbd.mesibajk

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.StringContains.containsString
//import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentTest{

    private lateinit var scenario : FragmentScenario<DetailsFragment>

    @Before
    fun setup(){
        val bundle = Bundle()
        bundle.putString("bikename", "Izmisljeno kolo")
        scenario = launchFragmentInContainer(fragmentArgs = bundle, themeResId = R.style.Theme_MESIbajk)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testCorrectNext(){
        val next = onView(withId(R.id.next))

        next.check(matches(withText("Ni podatkov o trenutni rezervaciji")))

    }

    @Test
    fun testCorrectLast(){
        val last = onView(withId(R.id.last))

        last.check(matches(withText("Ni podatkov o zadnji rezervaciji")))
    }

    @Test
    fun testCorrectKm(){
        val km = onView(withId(R.id.km))

        km.check(matches(withText(containsString("Število prevoženih kilometrov: "))))
    }

    @Test
    fun testCorrectSektor(){
        val oddelek = onView(withId(R.id.izposojaOddelek))

        oddelek.check(matches(withText(allOf(
            containsString("Število izposoj po oddelku:"),
            containsString("-Razvoj: "),
            containsString("-Prodaja: "),
            containsString("-Marketing: "),
            containsString("-Proizvodnja: ")))))
    }

    @Test
    fun testCorrectNamen(){
        val namen = onView(withId(R.id.izposojaNamen))

        namen.check(matches(withText(allOf(
            containsString("Število izposoj po namenih:"),
            containsString("-Službeni: "),
            containsString("-Privatni: ")))))
    }

}