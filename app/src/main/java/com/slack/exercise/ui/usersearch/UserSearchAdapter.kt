package com.slack.exercise.ui.usersearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slack.exercise.R
import com.slack.exercise.image.ImageLoader
import com.slack.exercise.model.UserSearchResult
import kotlinx.android.synthetic.main.item_user_search.view.*

/**
 * Adapter for the list of [UserSearchResult].
 */
class UserSearchAdapter(
    private val imageLoader: ImageLoader,
    context: Context
) : RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {

    private var userSearchResults: List<UserSearchResult> = emptyList()
    private val avatarRadius = context.resources.getDimensionPixelSize(R.dimen.avatar_corner_radius).toFloat()

    fun setResults(results: Set<UserSearchResult>) {
        userSearchResults = results.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return UserSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userSearchResults.size
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        imageLoader.loadRoundedImage(holder.userAvatar, userSearchResults[position].avatarUrl, avatarRadius)

        holder.displayName.text = userSearchResults[position].displayName
        holder.username.text = userSearchResults[position].username
    }

    class UserSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: ImageView = itemView.userAvatar
        val displayName: TextView = itemView.displayName
        val username: TextView = itemView.username
    }
}