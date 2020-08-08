package com.turelo.itunesbrowsersample.ui.browser

import android.app.Application
import android.view.View
import android.widget.ImageView
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import com.turelo.itunesbrowsersample.base.viewmodel.SingleLiveEvent
import com.turelo.itunesbrowsersample.models.ErrorWithRetryAction
import com.turelo.itunesbrowsersample.models.PagingStatus
import com.turelo.itunesbrowsersample.repositories.ITunesRepository
import com.turelo.itunesbrowsersample.ui.browser.models.SongItemViewModel
import com.turelo.itunesbrowsersample.ui.common.StatefulLayout
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import java.util.concurrent.TimeUnit

class BrowserViewModel(
    private val iTunesRepository: ITunesRepository,
    application: Application,
    private val subscribeOnSchedule: Scheduler
) : BaseViewModel(application) {

    companion object {
        val STATE_EMPTY_RESULT = "STATE_EMPTY_RESULT"
    }

    private val myPagingConfig = Config(
        pageSize = 20,
        prefetchDistance = 20,
        enablePlaceholders = true
    )

    private var loadMoreDisposable: Disposable? = null

    private var pagingStatus: PagingStatus? = null

    private var isLoadingMutableLiveData = MutableLiveData<Boolean>()
    fun isLoadingLiveData(): LiveData<Boolean> = isLoadingMutableLiveData

    var state = ObservableField<String>(StatefulLayout.State.CONTENT)

    private val errorMutableLiveData = SingleLiveEvent<ErrorWithRetryAction>()
    fun errorLiveData(): LiveData<ErrorWithRetryAction> = errorMutableLiveData

    private val searchSubject: PublishSubject<String> = PublishSubject.create()
    private val searchTextLiveData = LiveDataReactiveStreams.fromPublisher<String>(
        searchSubject.toFlowable(BackpressureStrategy.BUFFER)
            .debounce(300, TimeUnit.MILLISECONDS)
            .doOnNext {
                this.isLoadingMutableLiveData.postValue(true)
                this.populate(it)
            }
    )

    val songListLiveData: LiveData<PagedList<SongItemViewModel>> =
        this.searchTextLiveData.switchMap { term ->
            return@switchMap iTunesRepository.search(term).map {
                return@map SongItemViewModel(
                    trackId = it.trackId,
                    artistName = it.artistName ?: "",
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName ?: "",
                    trackName = it.trackName ?: "",
                    trackPrice = it.trackPrice
                )
            }.toLiveData(
                myPagingConfig
            )
        }


    fun search(text: String) {
        this.tag.d("Search: $text")
        searchSubject.onNext(text)
    }

    fun refresh() {
        this.populate(searchTextLiveData.value ?: "")
    }

    private fun populate(term: String) {
        this.pagingStatus = null
        this.compositeDisposable.clear()
        this.iTunesRepository.search(term, pagingStatus = PagingStatus())
            .subscribeOn(subscribeOnSchedule)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                this.tag.d("Search complete")

                this.state.set(
                    if (!it.next && it.page == 1) {
                        STATE_EMPTY_RESULT
                    } else {
                        StatefulLayout.State.CONTENT
                    }
                )

                this.pagingStatus = it
                this.isLoadingMutableLiveData.postValue(false)
            }, {
                this.tag.e(it)
                this.errorMutableLiveData.postValue(
                    ErrorWithRetryAction(
                        exception = it as Exception,
                        retry = {
                            this.populate(term)
                        })
                )
                this.isLoadingMutableLiveData.postValue(false)
            })
            .addTo(compositeDisposable)
    }

    fun loadMore() {
        if ((this.loadMoreDisposable == null || this.loadMoreDisposable!!.isDisposed) && this.pagingStatus != null) {
            this.loadMoreDisposable =
                this.iTunesRepository.search(
                    searchTextLiveData.value ?: "",
                    pagingStatus = this.pagingStatus!!
                )
                    .subscribeOn(subscribeOnSchedule)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        this.tag.d("Load more complete")
                        this.pagingStatus = it
                        this.tag.d(it.toString())
                        this.loadMoreDisposable = null
                    }, {
                        this.tag.e(it)
                        this.loadMoreDisposable = null
                        this.errorMutableLiveData.postValue(
                            ErrorWithRetryAction(
                                exception = it as Exception,
                                retry = {
                                    this.loadMore()
                                })
                        )
                    })
        }
    }

    fun itemSelect(data: SongItemViewModel, view: View) {
        val extras = FragmentNavigatorExtras(
            view.findViewById<ImageView>(R.id.artwork) to "artwork${data.trackId}"
            //view.findViewById<TextView>(R.id.collectionName) to "collectionName${data.trackId}"
        )

        val action =
            BrowserFragmentDirections.actionBrowserFragmentToDetailsFragment(
                collectionName = data.collectionName,
                artistName = data.artistName,
                artworkUrl100 = data.artworkUrl100,
                trackId = data.trackId
            )

        navigate(
            action,
            extras
        )
    }

    /*fun nextAction() {
        navigate(BrowserFragmentDirections.actionBrowserFragmentToDetailsFragment())
    }*/
}