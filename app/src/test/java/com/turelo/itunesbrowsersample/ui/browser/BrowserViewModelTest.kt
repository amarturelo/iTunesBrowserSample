package com.turelo.itunesbrowsersample.ui.browser

import androidx.lifecycle.Lifecycle
import androidx.room.Room
import com.turelo.itunesbrowsersample.common.AbstractViewModelTest
import com.turelo.itunesbrowsersample.data.db.ITunesDatabase
import com.turelo.itunesbrowsersample.data.providers.ITunesProvider
import com.turelo.itunesbrowsersample.data.providers.response.SearchResponse
import com.turelo.itunesbrowsersample.extensions.getOrAwaitValue
import com.turelo.itunesbrowsersample.models.ErrorWithRetryAction
import com.turelo.itunesbrowsersample.models.Song
import com.turelo.itunesbrowsersample.repositories.ITunesRepository
import com.turelo.itunesbrowsersample.repositories.impl.ITunesRepositoryImpl
import com.turelo.itunesbrowsersample.ui.common.CaptureObserver
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class BrowserViewModelTest : AbstractViewModelTest() {

    private lateinit var viewModel: BrowserViewModel
    private lateinit var iTunesRepository: ITunesRepository
    private lateinit var iTunesDatabase: ITunesDatabase
    private lateinit var iTunesProvider: ITunesProvider

    @Before
    fun setUp() {

        this.iTunesProvider = mock(ITunesProvider::class.java)

        this.iTunesDatabase = Room.inMemoryDatabaseBuilder(
            context,
            ITunesDatabase::class.java
        ).build()

        this.iTunesRepository = ITunesRepositoryImpl(
            songDao = this.iTunesDatabase.songDao(),
            iTunesProvider = this.iTunesProvider
        )

        this.viewModel = BrowserViewModel(
            iTunesRepository = iTunesRepository,
            application = context,
            subscribeOnSchedule = Schedulers.trampoline()
        )
    }

    @After
    fun closeDb() {
        iTunesDatabase.close()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    @Test
    fun notNullViewModel() {
        assertNotNull(viewModel)
    }

    @Test
    fun handleResult_WhenSuccessSearch() {
        val mockData = listOf(
            Song(
                trackId = 1,
                collectionName = "as",
                artistName = "as",
                artworkUrl100 = "as",
                trackPrice = 12.0,
                trackName = "as"
            ),
            Song(
                trackId = 2,
                collectionName = "as2",
                artistName = "as2",
                artworkUrl100 = "as2",
                trackPrice = 12.0,
                trackName = "as2"
            )
        )

        `when`(
            iTunesProvider.search(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Single.just(SearchResponse(2, mockData)))

        assertNull(viewModel.isLoadingLiveData().value)

        val observerIsLoading = CaptureObserver<Boolean>()

        viewModel.isLoadingLiveData().observeForever(observerIsLoading)

        viewModel.search("as")

        assertEquals(observerIsLoading.capture.size, 1)

        val songs = iTunesDatabase.songDao().getAll()
        assertEquals(songs.size, 2)
    }

    @Test
    fun handleError_WhenFailSearch() {

        `when`(
            iTunesProvider.search(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Single.error<SearchResponse>(Exception()))

        val observerIsLoading = CaptureObserver<Boolean>()
        val observerError = CaptureObserver<ErrorWithRetryAction>()

        viewModel.isLoadingLiveData().observeForever(observerIsLoading)
        viewModel.errorLiveData().observeForever(observerError)

        viewModel.search("as")

        viewModel.isLoadingLiveData().getOrAwaitValue()
        viewModel.errorLiveData().getOrAwaitValue()

        assertEquals(observerError.capture.size, 1)
        assertEquals(observerIsLoading.capture.size, 2)

    }

    @Test
    fun handleResult_WhenSearchAndRefresh() {

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        val mockData = listOf(
            Song(
                trackId = 1,
                collectionName = "as",
                artistName = "as",
                artworkUrl100 = "as",
                trackPrice = 12.0,
                trackName = "as"
            ),
            Song(
                trackId = 2,
                collectionName = "as2",
                artistName = "as2",
                artworkUrl100 = "as2",
                trackPrice = 12.0,
                trackName = "as2"
            )
        )

        `when`(
            iTunesProvider.search(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Single.just(SearchResponse(2, mockData)))

        assertNull(viewModel.isLoadingLiveData().value)

        val observerIsLoading = CaptureObserver<Boolean>()

        viewModel.isLoadingLiveData().observe(lifecycleOwner, observerIsLoading)

        viewModel.search("as")

        assertEquals(observerIsLoading.capture.size, 1)

        assertEquals(iTunesDatabase.songDao().getAll().size, 2)

        viewModel.refresh()

        assertEquals(iTunesDatabase.songDao().getAll().size, 2)

        print(observerIsLoading.capture)

        viewModel.isLoadingLiveData().getOrAwaitValue()

        assertEquals(observerIsLoading.capture.size, 3)
    }


}