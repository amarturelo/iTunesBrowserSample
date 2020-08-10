package com.turelo.itunesbrowsersample.di

import androidx.room.Room
import com.turelo.itunesbrowsersample.AppExecutors
import com.turelo.itunesbrowsersample.Player
import com.turelo.itunesbrowsersample.data.db.ITunesDatabase
import com.turelo.itunesbrowsersample.data.providers.ITunesProvider
import com.turelo.itunesbrowsersample.data.providers.impl.ITunesProviderImpl
import com.turelo.itunesbrowsersample.repositories.ITunesRepository
import com.turelo.itunesbrowsersample.repositories.impl.ITunesRepositoryImpl
import com.turelo.itunesbrowsersample.ui.browser.BrowserViewModel
import com.turelo.itunesbrowsersample.ui.details.DetailsViewModel
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelModule = module {
    viewModel {
        BrowserViewModel(
            application = get(),
            subscribeOnSchedule = Schedulers.from(get<AppExecutors>().networkIO()),
            iTunesRepository = get()
        )
    }
    viewModel {
        DetailsViewModel(
            application = get()
        )
    }
}

val providersModule = module {
    factory<ITunesProvider> {
        ITunesProviderImpl()
    }
}


val repositoriesModule = module {
    single<ITunesRepository> {
        ITunesRepositoryImpl(get(), get())
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
    factory {
        Player()
    }
}