package com.slack.exercise.ui.usersearch

import com.slack.exercise.dataprovider.RawDataProvider
import javax.inject.Inject

/**
 * if the search term matches or starts with anything found in the blacklist, no API call is performed
 * if a search term is not found in the blacklist, there is an API call to lookup matches
 * if the API returns no results, the search term is added to the blacklist
 */
class UserSearchController @Inject constructor(
    private val rawDataProvider: RawDataProvider
) {

    fun isSafeSearch(searchTerm: String): Boolean {
        val blacklist = rawDataProvider.readFromBlacklist() ?: return false

        if (blacklist.contains(searchTerm)) {
            return false
        }
        return true
    }
}