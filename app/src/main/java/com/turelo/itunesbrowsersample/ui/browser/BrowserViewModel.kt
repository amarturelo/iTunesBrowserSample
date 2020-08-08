package com.turelo.itunesbrowsersample.ui.browser

import android.app.Application
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import io.reactivex.Scheduler

class BrowserViewModel(
    application: Application,
    private val subscribeOnSchedule: Scheduler
) : BaseViewModel(application) {
    fun nextAction() {
        navigate(BrowserFragmentDirections.actionBrowserFragmentToDetailsFragment())
    }
}