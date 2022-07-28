package si.uni_lj.fri.pbd.mesibajk

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ReservationFragmentTest{

    private lateinit var scenario : FragmentScenario<ReservationFragment>

    @Before
    fun setup(){
        val bundle = Bundle()
        bundle.putString("bikename", "Hitro kolo")
        scenario = launchFragmentInContainer(fragmentArgs = bundle, themeResId = R.style.Theme_MESIbajk)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testEmptyName(){
        val name = ""
        onView(withId(R.id.izposojevalec)).perform(typeText(name))

        Espresso.closeSoftKeyboard()
        onView(withId(R.id.add)).perform(click())

        val editText = onView(withId(R.id.izposojevalec))

        //assertThat(onView(withId(R.id.izposojevalec)).check(matches(hasErrorText("Vsa polja morajo biti izpolnjena")))).isEqualTo(true)
        editText.check(matches(hasErrorText("Vsa polja morajo biti izpolnjena")))

    }

    @Test
    fun testCorrectName(){
        val name = "Niko Sok"
        onView(withId(R.id.izposojevalec)).perform(typeText(name))

        Espresso.closeSoftKeyboard()
        onView(withId(R.id.add)).perform(click())

        val editText = onView(withId(R.id.izposojevalec))
        assertThat(hasErrorText("Vsa polja morajo biti izpolnjena").matches(editText)).isEqualTo(false)

    }

}