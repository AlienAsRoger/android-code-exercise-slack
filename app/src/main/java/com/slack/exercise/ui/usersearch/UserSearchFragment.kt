package com.slack.exercise.ui.usersearch

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate
import com.slack.exercise.R
import com.slack.exercise.image.ImageLoader
import com.slack.exercise.model.UserSearchResult
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user_search.*
import kotterknife.bindView
import timber.log.Timber
import javax.inject.Inject


/**
 * Main fragment displaying and handling interactions with the view.
 * We use the MVP pattern and attach a Presenter that will be in charge of non view related operations.
 */
class UserSearchFragment : DaggerFragment(), UserSearchContract.View {

    private val userSearchResultList: RecyclerView by bindView(R.id.user_search_result_list)

    @Inject
    internal lateinit var presenter: UserSearchContract.Presenter

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_user_search, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpList()
    }

    override fun onStart() {
        super.onStart()

        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()

        presenter.detach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_search, menu)

        with(persistentSearchView) {
            setOnLeftBtnClickListener {
                activity?.finish()
            }
            setQueryInputHint(getString(R.string.search_users_hint))
            setQueryTextTypeface(ResourcesCompat.getFont(activity!!, R.font.lato_regular)!!)
            setSuggestionTextTypeface(ResourcesCompat.getFont(activity!!, R.font.lato_regular)!!)

            // Setting a delegate for the voice recognition input. Result will be delivered in Activity first
            setVoiceRecognitionDelegate(VoiceRecognitionDelegate(activity!!))

            setOnSearchQueryChangeListener { _, _, newQuery ->
                presenter.onQueryTextChange(newQuery)
            }

            setSuggestionsDisabled(true)
        }
    }

    override fun onUserSearchResults(results: Set<UserSearchResult>) {
        val adapter = userSearchResultList.adapter as UserSearchAdapter
        adapter.setResults(results)
    }

    override fun onUserSearchError(error: Throwable) {
        Timber.e(error, "Error searching users.")
    }

    private fun setUpToolbar() {
        val act = activity as UserSearchActivity
        act.setSupportActionBar(toolbar)
        act.supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowHomeEnabled(false)
        }
    }

    private fun setUpList() {
        with(userSearchResultList) {
            adapter = UserSearchAdapter(imageLoader, activity!!)
            val layoutManager = LinearLayoutManager(activity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            this.layoutManager = layoutManager
            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(context, layoutManager.orientation).apply {
                setDrawable(ContextCompat.getDrawable(context, R.drawable.list_divider)!!)
            })
        }
    }
}