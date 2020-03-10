package com.slack.exercise.dagger

import android.content.Context
import com.slack.exercise.db.UserDao
import com.slack.exercise.db.UserDatabase
import com.slack.exercise.utils.NetworkHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Offline dependency Module to provide singleton based instances.
 * Since they are using Application context it should be safe to pass it inside of Singleton
 */
@Module
object OfflineModule {

    @Singleton
    @Provides
    fun provideUserDao(context: Context?): UserDao {
        return UserDatabase.getDatabase(context!!).getUserDao()
    }

    @Singleton
    @Provides
    fun provideNetworkHelper(context: Context?): NetworkHelper {
        return NetworkHelper(context!!.applicationContext)
    }
}