package com.turelo.itunesbrowsersample.base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val tag = Timber.tag(javaClass.simpleName)

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private val _navigationCommands: MutableLiveData<NavigationCommand> =
        SingleLiveEvent()

    open val navigationCommands: LiveData<NavigationCommand>
        get() = _navigationCommands

    fun navigate(directions: NavDirections) {
        tag.d(directions.toString())

        _navigationCommands.postValue(
            NavigationCommand.To(
                directions
            )
        )
    }

    fun goBack() {
        tag.d("Back")
        _navigationCommands.postValue(NavigationCommand.Back)
    }
}