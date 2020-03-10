package com.slack.exercise.dataprovider

import com.slack.exercise.api.SlackApi
import com.slack.exercise.api.User
import com.slack.exercise.db.UserDao
import com.slack.exercise.model.UserSearchResult
import com.slack.exercise.model.toSearchResult
import com.slack.exercise.rx.RxSchedulersProvider
import com.slack.exercise.utils.EspressoIdlingResources
import com.slack.exercise.utils.ExperimentSettings
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [UserSearchResultDataProvider].
 */
@Singleton
class UserSearchResultDataProviderImpl @Inject constructor(
    private val rxSchedulersProvider: RxSchedulersProvider,
    private val userDao: UserDao,
    private val slackApi: SlackApi
) : UserSearchResultDataProvider {

    private var dbDisposable = Disposables.disposed()
    private var searchQueryDisposable = Disposables.disposed()

    private var usersListener: UserSearchResultListener? = null

    /**
     * This method implementation combines DB and API fetch and return results
     */
    override fun fetchUsers(searchTerm: String) {
        EspressoIdlingResources.incrementSearchResource()

        @Suppress("ConstantConditionIf")
        searchQueryDisposable = if (ExperimentSettings.isDbLoadEnabled) {
            Observable.concatArrayEager(getUsersFromDb(searchTerm), getUsersFromApi(searchTerm))
        } else {
            Observable.concatArrayEager(getUsersFromApi(searchTerm))
        }
            .subscribeOn(rxSchedulersProvider.IO)
            .observeOn(rxSchedulersProvider.main)
            // Drop DB data if we can fetch item fast enough from the API to avoid UI flickers
            .debounce(DEBOUNCE_TIME_MS, TimeUnit.MILLISECONDS)
            .subscribe({
                EspressoIdlingResources.decrementSearchResource()

                usersListener?.onSuccess(it)
            }, {
                EspressoIdlingResources.decrementSearchResource()

                if (it is UnknownHostException) {
                    getOfflineResults(searchTerm)
                } else {
                    usersListener?.onFail(it)
                }
            })
    }

    override fun setListener(listener: UserSearchResultListener) {
        usersListener = listener
    }

    override fun onDetach() {
        searchQueryDisposable.dispose()
        dbDisposable.dispose()
        usersListener = null
    }

    override fun loadOfflineResults(lastSearchedTerm: String) {
        dbDisposable = getOfflineResults(lastSearchedTerm)
    }

    private fun getOfflineResults(searchTerm: String): Disposable {
        return getUsersFromDb(searchTerm).subscribe({ users ->
            usersListener?.onSuccess(users)
        }, { error ->
            usersListener?.onFail(error)
        })
    }

    private fun getUsersFromDb(searchTerm: String): Observable<Set<UserSearchResult>> {
        val lowerCaseTerm = searchTerm.toLowerCase(Locale.getDefault())
        return userDao.getAllUsers()
            .subscribeOn(rxSchedulersProvider.IO)
            .observeOn(rxSchedulersProvider.main)
            .toObservable()
            .map {
                it.filter { user ->
                    user.displayName.toLowerCase(Locale.getDefault()).startsWith(lowerCaseTerm)
                        || user.username.toLowerCase(Locale.getDefault()).startsWith(lowerCaseTerm)
                }
            }
            .map { it.map { user -> user.toSearchResult() }.toSet() }
    }

    private fun getUsersFromApi(searchTerm: String): Observable<Set<UserSearchResult>> {
        return slackApi.searchUsers(searchTerm).toObservable()
            .doOnNext {
                storeUsersInDb(it)
            }
            .map { it.map { user -> user.toSearchResult() }.toSet() }
    }

    private fun storeUsersInDb(users: List<User>) {
        dbDisposable = Observable.fromCallable { userDao.insertAll(users) }
            .subscribe {}
    }

    companion object {
        private val DEBOUNCE_TIME_MS = TimeUnit.MILLISECONDS.toMillis(400)
    }
}