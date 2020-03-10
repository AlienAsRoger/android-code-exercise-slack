package com.slack.exercise.rx

import com.slack.exercise.utils.OpenClass
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * This class provides all default [Scheduler], can be overridden for tests
 */
@OpenClass
class RxSchedulersProvider @Inject constructor() {
    val main: Scheduler by lazy { AndroidSchedulers.mainThread() }
    val IO: Scheduler by lazy { Schedulers.io() }
    val compute: Scheduler by lazy { Schedulers.computation() }
}