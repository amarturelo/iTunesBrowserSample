package com.turelo.itunesbrowsersample.di

import com.turelo.itunesbrowsersample.AppExecutors
import com.turelo.itunesbrowsersample.ui.browser.BrowserViewModel
import com.turelo.itunesbrowsersample.ui.details.DetailsViewModel
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelModule = module {
    viewModel {
        BrowserViewModel(
            application = get(),
            subscribeOnSchedule = Schedulers.from(get<AppExecutors>().networkIO())
        )
    }
    viewModel {
        DetailsViewModel(
            application = get(),
            subscribeOnSchedule = Schedulers.from(get<AppExecutors>().networkIO())
        )
    }
}

val appModule = module {
    single {
        AppExecutors()
    }
}