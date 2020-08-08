package com.turelo.itunesbrowsersample.di

import androidx.room.Room
import com.turelo.itunesbrowsersample.AppExecutors
import com.turelo.itunesbrowsersample.data.db.ITunesDatabase
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

val dbModule = module {
    single {
        Room.databaseBuilder(get(), ITunesDatabase::class.java, "iTunes_database")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        get<ITunesDatabase>().songDao()
    }
}

val appModule = module {
    single {
        AppExecutors()
    }
}