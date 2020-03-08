package com.slack.exercise.ui.usersearch

import com.slack.exercise.TestUtils.BLACKLIST
import com.slack.exercise.TestUtils.mokk
import com.slack.exercise.dataprovider.RawDataProvider
import com.slack.exercise.dataprovider.SearchRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

/**
 * Set of tests to verify safe search functionality for [UserSearchController]
 * This test class uses mocks to verify methods invocation
 */
class UserSearchControllerMockTest {

    private val rawDataProvider: RawDataProvider = mokk()
    private val searchRepository: SearchRepository = mokk()
    private val controller = UserSearchController(rawDataProvider, searchRepository)

    @Before
    fun setUp() {
        given(rawDataProvider.readFromBlacklist()).willReturn(BLACKLIST)
    }

    @Test
    fun `should init repository only once`() {
        controller.shouldStartSearch("bad")

        given(searchRepository.isInitialized).willReturn(true)

        controller.shouldStartSearch("good")

        // default parameter is times(1), so we expect only one invocation
        verify(rawDataProvider).readFromBlacklist()
        verify(searchRepository).init(BLACKLIST)
    }

    @Test
    fun `should NOT add empty string to last search`() {
        val term = ""
        assertTrue(controller.shouldStartSearch(term))

        controller.restrictLastSearch()

        verify(searchRepository, never()).addToList(term)
    }
}
