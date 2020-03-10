package com.slack.exercise.ui.usersearch


import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    var activityTestRule = ActivityTestRule(UserSearchActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.getSearchQueryIdlingResource())
    }

    @Test
    fun should_Find_User_And_Survive_Rotation() {
        onView(allOf(withId(R.id.inputEt), isDisplayed())).perform(replaceText("pa"), closeSoftKeyboard())

        onView(allOf(withId(R.id.displayName), withText(TEST_USER))).check(matches(withText(TEST_USER)))

        rotateScreen()

        onView(allOf(withId(R.id.displayName), withText(TEST_USER))).check(matches(withText(TEST_USER)))
    }

    private fun rotateScreen() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val orientation = context.resources.configuration.orientation

        activityTestRule.activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    companion object {
        private const val TEST_USER = "Payton Jones"
    }
}
