package com.slack.exercise.dataprovider

import com.slack.exercise.model.UserSearchResult

/**
 * Provider of [UserSearchResult].
 * This interface abstracts the logic of searching for users through the available data sources.
 */
interface UserSearchResultDataProvider {

    /**
     * Request from available data sources to find users matching searchTerm
     */
    fun fetchUsers(searchTerm: String)

    /**
     * Sets a listener that will be informed about results
     */
    fun setListener(listener: UserSearchResultListener)

    /**
     * Callback to release resources and stop operations
     */
    fun onDetach()

    /**
     * Request to load last searched results from local storage
     */
    fun loadOfflineResults(lastSearchedTerm: String)
}