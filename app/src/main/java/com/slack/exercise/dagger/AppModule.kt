package com.slack.exercise.dagger

import android.app.Application
import android.content.Context
import com.slack.exercise.api.SlackApi
import com.slack.exercise.api.SlackApiImpl
import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.dataprovider.UserSearchResultDataProviderImpl
import dagger.Binds
import dagger.Module

/**
 * Module to setup Application scoped instances that require providers.
 */
@Suppress("unused")
@Module
abstract class AppModule {
    @Binds
    abstract fun provideUserSearchResultDataProvider(dataProvider: UserSearchResultDataProviderImpl): UserSearchResultDataProvider

    @Binds
    abstract fun provideSlackApi(apiImpl: SlackApiImpl): SlackApi

    @Binds
    abstract fun provideContext(application: Application?): Context?
}