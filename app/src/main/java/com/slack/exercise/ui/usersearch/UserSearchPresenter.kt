package com.slack.exercise.ui.usersearch

import android.os.Bundle
import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.dataprovider.UserSearchResultListener
import com.slack.exercise.model.UserSearchResult
import javax.inject.Inject

/**
 * Presenter responsible for reacting to user inputs and initiating search queries.
 */
class UserSearchPresenter @Inject constructor(
    private val userNameResultDataProvider: UserSearchResultDataProvider,
    private val userSearchController: UserSearchController
) : UserSearchContract.Presenter, UserSearchResultListener {

    private var lastSearchTerm: String = ""
    private var searchResults: Set<UserSearchResult>? = null
    private var view: UserSearchContract.View? = null

    override fun attach(view: UserSearchContract.View) {
        this.view = view

        userNameResultDataProvider.setListener(this)

        searchResults?.let { view.onUserSearchResults(it) }
    }

    override fun onSuccess(results: Set<UserSearchResult>) {
        searchResults = results
        if (results.isEmpty()) {
            userSearchController.restrictLastSearch()
        }
        view?.onUserSearchResults(results)
    }

    override fun onFail(error: Throwable) {
        view?.onUserSearchError(error)
    }

    override fun detach() {
        view = null
        userNameResultDataProvider.onDetach()
    }

    override fun onQueryTextChange(searchTerm: String) {
        lastSearchTerm = searchTerm
        if (userSearchController.shouldStartSearch(searchTerm)) {
            userNameResultDataProvider.fetchUsers(searchTerm)
        } else { // we don't expect search request, so clear results
            searchResults = emptySet()
            view?.onUserSearchResults(emptySet())
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