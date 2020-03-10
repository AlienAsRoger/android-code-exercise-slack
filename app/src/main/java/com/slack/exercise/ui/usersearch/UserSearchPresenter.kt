package com.slack.exercise.ui.usersearch

import android.os.Bundle
import com.slack.exercise.dataprovider.SearchRepository
import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.dataprovider.UserSearchResultListener
import com.slack.exercise.model.UserSearchResult
import com.slack.exercise.utils.NetworkHelper
import javax.inject.Inject

/**
 * Presenter responsible for reacting to user inputs and initiating search queries.
 */
class UserSearchPresenter @Inject constructor(
    private val userNameResultDataProvider: UserSearchResultDataProvider,
    private val userSearchController: UserSearchController,
    private val networkHelper: NetworkHelper,
    private val searchRepository: SearchRepository
) : UserSearchContract.Presenter, UserSearchResultListener {

    /**
     * This flag avoid receiving results when user quickly erase full query, but previous requests are still coming.
     */
    private var isSearchRequestCanceled: Boolean = false
    private var lastSearchTerm: String = ""
    private var searchResults: Set<UserSearchResult>? = null
    private var view: UserSearchContract.View? = null

    override fun attach(view: UserSearchContract.View) {
        this.view = view

        userNameResultDataProvider.setListener(this)

        // If user is offline, show last searched results
        if (!networkHelper.isConnected() && !searchRepository.getLastSearchedTerm().isNullOrEmpty()) {
            view.showLoadingView(true)
            userNameResultDataProvider.loadOfflineResults(searchRepository.getLastSearchedTerm()!!)
            view.showMessageAboutOffline()
        }

        if (!searchResults.isNullOrEmpty()) {
            searchResults?.let { view.onUserSearchResults(it) }
        } else if (lastSearchTerm.isEmpty()) {
            view.showEmptyState()
        } else {
            view.showNoResults()
        }
    }

    override fun onSuccess(results: Set<UserSearchResult>) {
        view?.showLoadingView(false)
        if (isSearchRequestCanceled) {
            return
        }

        searchResults = results
        if (results.isEmpty()) {
            // Don't restrict last search for offline mode
            if (networkHelper.isConnected()) {
                userSearchController.restrictLastSearch()
                view?.showNoResults()
            } else {
                view?.showNoResultsInOffline()
            }
        } else {
            searchRepository.setLastSearchedTerm(lastSearchTerm)

            view?.onUserSearchResults(results)
        }
    }

    override fun onFail(error: Throwable) {
        view?.onUserSearchError(error)
    }

    override fun detach() {
        view = null
        userNameResultDataProvider.onDetach()
    }

    override fun onQueryTextChange(searchTerm: String) {
        isSearchRequestCanceled = false
        lastSearchTerm = searchTerm

        if (userSearchController.shouldStartSearch(searchTerm)) {
            view?.showLoadingView(true)
            userNameResultDataProvider.fetchUsers(searchTerm)
        } else { // we don't expect search request, so clear results
            view?.showLoadingView(false)
            isSearchRequestCanceled = true
            searchResults = emptySet()

            if (lastSearchTerm.isEmpty()) {
                view?.showEmptyState()
            } else {
                view?.showNoResults()
            }
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            it.getParcelableArray(RESULT_LIST)?.let { resultList ->
                @Suppress("UNCHECKED_CAST")
                searchResults = resultList.toSet() as Set<UserSearchResult>
            }
            lastSearchTerm = it.getString(LAST_SEARCH_TERM, "")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (lastSearchTerm.isNotEmpty()) {
            searchResults?.let { outState.putParcelableArray(RESULT_LIST, it.toTypedArray()) }
        }
        outState.putString(LAST_SEARCH_TERM, lastSearchTerm)
    }

    companion object {
        private const val RESULT_LIST = "result_list"
        private const val LAST_SEARCH_TERM = "las_search_term"
    }

}