package com.slack.exercise.ui.usersearch

import com.slack.exercise.TestUtils.BLACKLIST
import com.slack.exercise.TestUtils.mokk
import com.slack.exercise.dataprovider.RawDataProvider
import com.slack.exercise.dataprovider.SearchRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given

/**
 * Set of tests to verify safe search functionality for [UserSearchController].
 * This class uses TestSearchRepository as part of integration test
 */
class UserSearchControllerTest {

    private val rawDataProvider: RawDataProvider = mokk()
    private val searchRepository: SearchRepository = TestSearchRepository()
    private val controller = UserSearchController(rawDataProvider, searchRepository)

    @Before
    fun setUp() {
        given(rawDataProvider.readFromBlacklist()).willReturn(BLACKLIST)
    }

    /** if a search term is not found in the blacklist, there is an API call to lookup matches */
    @Test
    fun `should allow search - no match in blacklist`() {
        assertTrue(controller.shouldStartSearch("term"))

        assertTrue(searchRepository.isInitialized)
    }

    @Test
    fun `should NOT allow search - agf starts with ag`() {
        assertFalse(controller.shouldStartSearch("agf"))
    }

    /** if the search term matches or starts with anything found in the blacklist, no API call is performed */
    @Test
    fun `should NOT allow search - sarah starts with sara`() {
        assertFalse(controller.shouldStartSearch("sarah"))
    }

    @Test
    fun `should NOT allow search - all items from blacklist`() {
        for (term in BLACKLIST.split("\n")) {
            assertFalse(controller.shouldStartSearch(term))
        }
    }

    /** if the API returns no results, the search term is added to the blacklist */
    @Test
    fun `should NOT allow search - last result was empty`() {
        val term = "ssn"
        assertTrue(controller.shouldStartSearch(term))

        controller.restrictLastSearch()

        assertFalse(controller.shouldStartSearch(term))
    }
}

/**
 * Faster version of original implementation that uses RAM for storing
 */
class TestSearchRepository : SearchRepository {

    private val searchSet = HashSet<String>()

    override var isInitialized: Boolean = false

    override fun init(blacklist: String) {
        val terms = blacklist.split("\n")

        // save terms into set for quick access
        for (term in terms) {
            searchSet.add(term)
        }
        isInitialized = true
    }

    override fun matches(searchTerm: String): Boolean {
        return searchSet.contains(searchTerm)
    }

    override fun startsWith(searchTerm: String): Boolean {
        searchSet.forEach {
            val result = searchTerm.startsWith(it)
            if (result) {
                return true
            }
        }
        return false
    }

    override fun addToList(lastSearchTerm: String) {
        searchSet.add(lastSearchTerm)
    }

}