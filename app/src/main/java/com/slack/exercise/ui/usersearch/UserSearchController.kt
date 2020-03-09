package com.slack.exercise.ui.usersearch

import com.slack.exercise.dataprovider.RawDataProvider
import com.slack.exercise.dataprovider.SearchRepository
import javax.inject.Inject

/**
 * This controller is responsible for checking whether we should start search or not based on given term and conditions:
 *
 * - if the search term matches or starts with anything found in the blacklist, no API call is performed
 * - if a search term is not found in the blacklist, there is an API call to lookup matches
 * - if the API returns no results, the search term is added to the blacklist
 */
class UserSearchController @Inject constructor(
    private val rawDataProvider: RawDataProvider,
    private val searchRepository: SearchRepository
) {

    private var lastSearchTerm: String = ""

    fun shouldStartSearch(searchTerm: String): Boolean {
        if (searchTerm.isEmpty()) {
            return false
        }
        if (!searchRepository.isInitialized) {
            val blacklist = rawDataProvider.readFromBlacklist() ?: return false
            searchRepository.init(blacklist)
        }

        if (searchRepository.matches(searchTerm)) {
            return false
        }
        lastSearchTerm = searchTerm
        return true
    }

    /**
     * Add last search term to list, except empty string, since all words starts with ""
     */
    fun restrictLastSearch() {
        if (lastSearchTerm.isNotEmpty()) {
            searchRepository.addToList(lastSearchTerm)
        }
    }
}