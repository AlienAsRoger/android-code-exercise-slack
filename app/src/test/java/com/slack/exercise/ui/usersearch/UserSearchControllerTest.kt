package com.slack.exercise.ui.usersearch

import com.slack.exercise.TestUtils.BLACKLIST
import com.slack.exercise.TestUtils.mokk
import com.slack.exercise.dataprovider.RawDataProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given

/**
 * Set of tests to verify safe search functionality for [UserSearchController]
 */
class UserSearchControllerTest {

    private val rawDataProvider: RawDataProvider = mokk()
    private val controller = UserSearchController(rawDataProvider)

    @Before
    fun setUp() {
        given(rawDataProvider.readFromBlacklist()).willReturn(BLACKLIST)
    }

    @Test
    fun `should allow search`() {
        assertTrue(controller.isSafeSearch("term"))
    }

    @Test
    fun `should NOT allow search - all items from blacklist`() {
        for (term in BLACKLIST.split("\n")) {
            assertFalse(controller.isSafeSearch(term))
        }
    }
}