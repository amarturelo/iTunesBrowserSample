package com.turelo.itunesbrowsersample.ui.browser

import androidx.room.Room
import com.turelo.itunesbrowsersample.common.AbstractViewModelTest
import com.turelo.itunesbrowsersample.data.db.ITunesDatabase
import com.turelo.itunesbrowsersample.models.Song
import com.turelo.itunesbrowsersample.repositories.ITunesRepository
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.mock


class BrowserViewModelTest : AbstractViewModelTest() {

    lateinit var viewModel: BrowserViewModel
    lateinit var iTunesRepository: ITunesRepository
    private lateinit var iTunesDatabase: ITunesDatabase

    @Before
    fun setUp() {
        iTunesDatabase = Room.inMemoryDatabaseBuilder(
            context,
            ITunesDatabase::class.java
        ).build()

        iTunesRepository = mock(ITunesRepository::class.java)
        viewModel = BrowserViewModel(
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
    fun testLo() {
        iTunesDatabase.songDao().insertAll(
            listOf(
                Song(
                    trackId = 12,
                    collectionName = "as",
                    artistName = "as",
                    artworkUrl100 = "as",
                    trackPrice = 12.0,
                    trackName = "as"
                )
            )
        )

        /*Mockito.`when`(this.iTunesRepository.search(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer iTunesDatabase.songDao().pagingSource(it.getArgument(0))
        }

        viewModel.search("as")*/

        val songs = iTunesDatabase.songDao().getAll()
        assertEquals(songs.size, 1)
    }

}