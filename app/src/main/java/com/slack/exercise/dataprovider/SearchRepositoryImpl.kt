package com.slack.exercise.dataprovider

import android.content.Context

/**
 * Implementation of [SearchRepository] based on SharedPreferences and hashSet
 */
class SearchRepositoryImpl(context: Context) : SearchRepository {

    private val sharedPref = context.getSharedPreferences(SEARCH_PREF_KEY, Context.MODE_PRIVATE)

    private val searchSet = HashSet<String>()
    private var originalList = ""

    override var isInitialized: Boolean = false

    override fun init(blacklist: String) {
        // save list into local storage that we can update with invalid search results
        sharedPref.edit().apply {
            putString(LIST_PREF_KEY, blacklist).apply()
        }

        originalList = blacklist
        val terms = blacklist.split("\n")

        // save terms into Set for a quick access
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

        // save to permanent storage
        originalList += "\n$lastSearchTerm"
        sharedPref.edit().apply {
            putString(LIST_PREF_KEY, originalList).apply()
        }
    }

    companion object {
        private const val SEARCH_PREF_KEY = "search_pref_key"
        private const val LIST_PREF_KEY = "list_pref_key"
    }

}