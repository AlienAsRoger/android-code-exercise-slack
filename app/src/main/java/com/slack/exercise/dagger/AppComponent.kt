package com.slack.exercise.dagger

import android.app.Application
import com.slack.exercise.App
import com.slack.exercise.ui.dagger.BindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

/**
 * Component providing Application scoped instances.
 */
@Singleton
@Component(modules = [AppModule::class, AndroidInjectionModule::class, BindingModule::class, OfflineModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {

    /**
     * Inject our Application implementation so it can be provided as dependency
     */
    fun inject(app: App?)

    override fun inject(instance: DaggerApplication?)

    /**
     * Builder interface that allows to inject our [Application] into [AppComponent] graph
     */
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder
        fun build(): AppComponent
    }
}