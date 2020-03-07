package com.slack.exercise.ui.dagger

import javax.inject.Scope

/**
 * Identifies a type that the injector only instantiates once per Activity instance. Not inherited.
 *
 * @see [Scope]
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope