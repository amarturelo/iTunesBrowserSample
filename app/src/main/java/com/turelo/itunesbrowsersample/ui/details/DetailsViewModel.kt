package com.turelo.itunesbrowsersample.ui.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import io.reactivex.Scheduler

class DetailsViewModel(
    application: Application
) : BaseViewModel(application) {
    private lateinit var arg: DetailsFragmentArgs

    private val collectionNameMutableLiveData = MutableLiveData<String>()
    private val artistsNameMutableLiveData = MutableLiveData<String>()
    private val artworkMutableLiveData = MutableLiveData<String>()


    fun with(arg: DetailsFragmentArgs) {
        this.arg = arg

        this.collectionNameMutableLiveData.value = arg.collectionName
        this.artistsNameMutableLiveData.value = arg.artistName
        this.artworkMutableLiveData.value = arg.artworkUrl100
    }

    fun collectionNameLiveData(): LiveData<String> = collectionNameMutableLiveData
    fun artistsNameLiveData(): LiveData<String> = artistsNameMutableLiveData
    fun artworkLiveData(): LiveData<String> = artworkMutableLiveData
}