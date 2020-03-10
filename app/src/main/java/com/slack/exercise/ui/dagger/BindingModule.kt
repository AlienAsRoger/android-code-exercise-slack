package com.slack.exercise.ui.dagger

import com.slack.exercise.dataprovider.RawDataProvider
import com.slack.exercise.dataprovider.SearchRepository
import com.slack.exercise.dataprovider.SearchRepositoryImpl
import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.image.ImageLoader
import com.slack.exercise.image.picasso.PicassoImageLoader
import com.slack.exercise.ui.usersearch.*
import com.slack.exercise.utils.NetworkHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Module to declare UI components that have injectable members
 */
@Suppress("unused")
@Module
abstract class BindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun bindUserSearchActivity(): UserSearchActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun bindUserSearchFragment(): UserSearchFragment

    @Binds
    abstract fun bindsImageLoader(impl: PicassoImageLoader): ImageLoader

}

/**
 * Module to provide dependencies with Activity/Fragments limited by [ActivityScope]
 */
@Suppress("unused")
@Module
object SearchModule {

    /**
     * Despite having @Inject at constructor, UserSearchPresenter can't be inserted properly if referenced by interface
     * in UserSearchFragment
     */
    @ActivityScope
    @Provides
    fun provideUserUserSearchPresenter(
        userNameResultDataProvider: UserSearchResultDataProvider,
        userSearchController: UserSearchController,
        networkHelper: NetworkHelper,
        searchRepository: SearchRepository
    ): UserSearchContract.Presenter {
        return UserSearchPresenter(userNameResultDataProvider, userSearchController, networkHelper, searchRepository)
    }

    @ActivityScope
    @Provides
    fun provideRawDataProvider(fragment: UserSearchFragment): RawDataProvider {
        return RawDataProvider(fragment.activity!!)
    }

    @ActivityScope
    @Provides
    fun provideSearchRepositoryImpl(fragment: UserSearchFragment): SearchRepository {
        return SearchRepositoryImpl(fragment.activity!!)
    }
}