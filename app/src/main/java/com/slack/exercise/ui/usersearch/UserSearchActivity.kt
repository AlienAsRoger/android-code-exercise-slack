package com.slack.exercise.ui.usersearch

import android.content.Intent
import android.os.Bundle
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate
import com.slack.exercise.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.fragment_user_search.*

/**
 * Launcher activity. Kept light and simple to delegate view logic to fragment(s) it attaches.
 */
class UserSearchActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)
    }

    /**
     * Have to be declared in activity level as Voice recognition will send result to an activity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data)
    }
}
