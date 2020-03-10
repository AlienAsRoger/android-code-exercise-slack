package com.slack.exercise.model

import android.os.Parcelable
import com.slack.exercise.api.User
import kotlinx.android.parcel.Parcelize

/**
 * Models users returned by the API.
 */
@Parcelize
data class UserSearchResult(
    val displayName: String,
    val username: String,
    val avatarUrl: String
) : Parcelable

fun User.toSearchResult(): UserSearchResult {
    return UserSearchResult(displayName, username, avatarUrl)
}