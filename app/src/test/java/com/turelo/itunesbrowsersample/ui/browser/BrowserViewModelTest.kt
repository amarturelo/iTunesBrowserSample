package com.turelo.itunesbrowsersample.ui.browser

import androidx.room.Room
import com.trello.rxlifecycle3.LifecycleProvider
import com.turelo.itunesbrowsersample.common.AbstractViewModelTest
import com.turelo.itunesbrowsersample.data.db.ITunesDatabase
import com.turelo.itunesbrowsersample.data.providers.ITunesProvider
import com.turelo.itunesbrowsersample.data.providers.response.SearchResponse
import com.turelo.itunesbrowsersample.models.Song
import com.turelo.itunesbrowsersample.repositories.ITunesRepository
import com.turelo.itunesbrowsersample.repositories.impl.ITunesRepositoryImpl
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*


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
    }

    @Test
    fun notNullViewModel() {
        assertNotNull(viewModel)
    }

    @Test
    fun cacheSearch_WhenSuccessProvider() {
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
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Single.just(SearchResponse(2, mockData)))

        viewModel.search("as")

        val songs = iTunesDatabase.songDao().getAll()
        assertEquals(songs.size, 2)
    }

}