package com.slack.exercise.dataprovider

import androidx.annotation.UiThread
import com.slack.exercise.model.UserSearchResult

/**
 * Interface for communicating results back to [com.slack.exercise.ui.usersearch.UserSearchPresenter]
 */
interface UserSearchResultListener {

    @UiThread
    fun onSuccess(results: Set<UserSearchResult>)

    @UiThread
    fun onFail(error: Throwable)
}