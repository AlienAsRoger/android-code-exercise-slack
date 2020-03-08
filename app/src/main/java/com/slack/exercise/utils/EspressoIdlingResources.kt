package com.slack.exercise.utils

import androidx.test.espresso.idling.CountingIdlingResource
import com.slack.exercise.BuildConfig

/**
 * Static utility class that helps with async operations for UI tests using Espresso
 * All real operations are only happening on Debug build
 */
object EspressoIdlingResources {

    private val searchQueryIdlingResource = CountingIdlingResource("Search Query")

    /**
     * IdlingResource used for typing in a Search query and then waiting for the result
     * Getter used only in tests, main code uses increment/decrementSearchQueryIdlingResource()
     */
    fun getSearchQueryIdlingResource(): CountingIdlingResource? {
        return searchQueryIdlingResource
    }

    fun incrementSearchResource() {
        if (BuildConfig.DEBUG && searchQueryIdlingResource.isIdleNow) {
            searchQueryIdlingResource.increment()
        }
    }

    fun decrementSearchResource() {
        if (BuildConfig.DEBUG && !searchQueryIdlingResource.isIdleNow) {
            searchQueryIdlingResource.decrement()
        }
    }
}