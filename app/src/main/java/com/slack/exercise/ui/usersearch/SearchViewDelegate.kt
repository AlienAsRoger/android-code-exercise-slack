package com.slack.exercise.ui.usersearch

import androidx.core.content.res.ResourcesCompat
import com.slack.exercise.R
import kotlinx.android.synthetic.main.fragment_user_search.*
import javax.inject.Inject

/**
 * This class delegates responsibility of dealing with searchView 3rd party library to make UI code lightweight
 */
class SearchViewDelegate @Inject constructor(
    private val presenter: UserSearchContract.Presenter,
    private val fragment: UserSearchFragment
) {

    fun setUpSearchView() {
        val activity = fragment.activity ?: return

        with(fragment.persistentSearchView) {
            setOnLeftBtnClickListener {
                activity.finish()
            }
            setQueryInputHint(activity.getString(R.string.search_users_hint))
            setQueryInputTextColor(ResourcesCompat.getColor(activity.resources, R.color.black, activity.theme))
            setQueryTextTypeface(ResourcesCompat.getFont(activity, R.font.lato_regular)!!)
            setSuggestionTextTypeface(ResourcesCompat.getFont(activity, R.font.lato_regular)!!)

            setProgressBarColor(ResourcesCompat.getColor(activity.resources, R.color.grey, activity.theme))

            // Setting a delegate for the voice recognition input. Result will be delivered in Activity first
            setVoiceRecognitionDelegate(com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate(activity))

            setOnSearchQueryChangeListener { _, _, newQuery ->
                presenter.onQueryTextChange(newQuery)
            }

            setSuggestionsDisabled(true)
        }
    }

    fun showProgressBar(show: Boolean) {
        fragment.persistentSearchView?.let {
            if (show) {
                it.showProgressBar(true)
                it.hideLeftButton(true)
            } else {
                it.showLeftButton(true)
                it.hideProgressBar(true)
            }
        }
    }
}