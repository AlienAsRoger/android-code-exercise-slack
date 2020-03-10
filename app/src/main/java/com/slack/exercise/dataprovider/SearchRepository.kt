package com.slack.exercise.dataprovider

/**
 * Interface for storing and accessing data related to search results
 */
interface SearchRepository {

    var isInitialized: Boolean

    /**
     * Saves blacklist into local storage that can be updated with new terms
     */
    fun init(blacklist: String)

    /**
     * Checks whether new search term matches any saved terms in blacklist
     */
    fun matches(searchTerm: String): Boolean

    /**
     * Adds new terms to blacklist
     */
    fun addToList(lastSearchTerm: String)

    /**
     * Retrieve last non empty search term
     */
    fun getLastSearchedTerm(): String?

    /**
     * Should set last searched search term if it's not empty
     */
    fun setLastSearchedTerm(searchTerm: String)

}