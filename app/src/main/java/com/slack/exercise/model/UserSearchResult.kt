package com.slack.exercise.model

import com.slack.exercise.api.User

/**
 * Models users returned by the API.
 */
data class UserSearchResult(
    val displayName: String,
    val username: String,
    val avatarUrl: String
)

fun User.toSearchResult(): UserSearchResult {
    return UserSearchResult(displayName, username, avatarUrl)
}