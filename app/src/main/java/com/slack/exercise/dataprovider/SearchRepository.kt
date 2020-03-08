package com.slack.exercise.dataprovider

/**
 * Interface for storing and accessing data related to search results
 */
interface SearchRepository {

    var isInitialized: Boolean

    fun init(blacklist: String)
    fun matches(searchTerm: String): Boolean
    fun startsWith(searchTerm: String): Boolean
    fun addToList(lastSearchTerm: String)
}