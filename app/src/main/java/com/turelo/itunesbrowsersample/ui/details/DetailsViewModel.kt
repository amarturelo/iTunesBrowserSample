package com.turelo.itunesbrowsersample.ui.details

import android.app.Application
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import io.reactivex.Scheduler

class DetailsViewModel(
    application: Application,
    private val subscribeOnSchedule: Scheduler
) : BaseViewModel(application) {
}