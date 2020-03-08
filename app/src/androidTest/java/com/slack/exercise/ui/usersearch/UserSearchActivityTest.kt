package com.slack.exercise.ui.usersearch


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.slack.exercise.R
import com.slack.exercise.utils.EspressoIdlingResources
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Simple UI test that runs application and verifies that search is working
 */
@RunWith(AndroidJUnit4::class)
class UserSearchActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(UserSearchActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.getSearchQueryIdlingResource())
    }

    @Test
    fun userSearchActivityTest() {
        onView(allOf(withId(R.id.inputEt), isDisplayed())).perform(replaceText("pa"), closeSoftKeyboard())

        onView(allOf(withId(R.id.displayName), withText("Payton Jones"))).check(matches(withText("Payton Jones")))
    }
}
