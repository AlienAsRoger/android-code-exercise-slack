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
        val wasInitBefore = sharedPref.getBoolean(INIT_PREF_KEY, false)

        originalList = if (wasInitBefore) {
            sharedPref.getString(LIST_PREF_KEY, "")!!
        } else {
            // save list into local storage that we can update with invalid search results
            sharedPref.edit().apply {
                putString(LIST_PREF_KEY, blacklist).apply()
                putBoolean(INIT_PREF_KEY, true).apply()
            }
            blacklist
        }

        val terms = originalList.split("\n")

        // save terms into Set for a quick access
        for (term in terms) {
            searchSet.add(term)
        }
        isInitialized = true
    }

    override fun matches(searchTerm: String): Boolean {
        return Companion.matches(searchTerm, searchSet)
    }

    override fun addToList(lastSearchTerm: String) {
        searchSet.add(lastSearchTerm)

        // save to permanent storage
        originalList += "\n$lastSearchTerm"
        sharedPref.edit().apply {
            putString(LIST_PREF_KEY, originalList).apply()
        }
    }

    override fun getLastSearchedTerm(): String? {
        return sharedPref.getString(LAST_TERM_PREF_KEY, null)
    }

    override fun setLastSearchedTerm(searchTerm: String) {
        if (searchTerm.isBlank()) {
            return
        }
        sharedPref.edit().apply {
            putString(LAST_TERM_PREF_KEY, searchTerm).apply()
        }
    }

    companion object {
        private const val SEARCH_PREF_KEY = "search_pref_key"
        private const val LIST_PREF_KEY = "list_pref_key"
        private const val INIT_PREF_KEY = "init_pref_key"
        private const val LAST_TERM_PREF_KEY = "last_term_pref_key"

        fun matches(searchTerm: String, searchSet: Set<String>): Boolean {
            return searchSet.contains(searchTerm) || startsWith(searchTerm, searchSet)
        }

        private fun startsWith(searchTerm: String, searchSet: Set<String>): Boolean {
            searchSet.forEach {
                val result = searchTerm.startsWith(it)
                if (result) {
                    return true
                }
            }
            return false
        }
    }

}