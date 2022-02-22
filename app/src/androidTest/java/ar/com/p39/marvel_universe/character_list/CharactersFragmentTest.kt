package ar.com.p39.marvel_universe.character_list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ar.com.p39.marvel_universe.CustomMatchers.atPosition
import ar.com.p39.marvel_universe.DisableAnimationsRule
import ar.com.p39.marvel_universe.MainActivity
import ar.com.p39.marvel_universe.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

/*
 * This app is very simple, so I did not wanted to make this tests bloated.
 *
 * In a more complex application I would recommend to move to a Robot Pattern testing as
 * described in https://medium.com/android-bits/espresso-robot-pattern-in-kotlin-fc820ce250f7
 */
@HiltAndroidTest
class CharactersFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var animationsRule = DisableAnimationsRule()

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun listIsVisible() {
        onView(withId(R.id.list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun characterMoreInfo() {
        onView(withId(R.id.list))
            .check(matches(atPosition(0, hasDescendant(withText("More info")))))
    }

    @Test
    fun characterDetails() {
        // TODO This only works because we have 1 item in the list, it can be improved
        onView(withText("More info"))
            .perform(click());

        onView(withId(R.id.navigation))
            .check(matches(isDisplayed()))
    }
}