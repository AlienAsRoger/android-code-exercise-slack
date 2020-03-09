package com.slack.exercise.dagger

import android.content.Context
import com.slack.exercise.db.UserDao
import com.slack.exercise.db.UserDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Room dependency Module to provide singleton based instance for [UserDao]
 */
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideUserDao(context: Context?): UserDao {
        return UserDatabase.getDatabase(context!!).getUserDao()
    }
}