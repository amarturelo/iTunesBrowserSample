package com.turelo.itunesbrowsersample.data.db.dao

import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.turelo.itunesbrowsersample.ITunesBrowserApp
import com.turelo.itunesbrowsersample.common.AbstractBaseTest
import com.turelo.itunesbrowsersample.data.db.ITunesDatabase
import com.turelo.itunesbrowsersample.models.Song
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongDaoTest : AbstractBaseTest() {

    val context = ApplicationProvider.getApplicationContext<ITunesBrowserApp>()


    private lateinit var iTunesDatabase: ITunesDatabase

    @Before
    fun initDb() {
        iTunesDatabase = Room.inMemoryDatabaseBuilder(
            context,
            ITunesDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        iTunesDatabase.close()
    }

    @Test
    fun insertSong_WhenSavesData() {
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
        iTunesDatabase.songDao().insertAll(mockData)
        val allSong = iTunesDatabase.songDao().getAll()
        Assert.assertEquals(allSong.size, mockData.size)
    }

    @Test
    fun clearCache_WhenEraseData() {
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
        iTunesDatabase.songDao().insertAll(mockData)
        val allSongBefore = iTunesDatabase.songDao().getAll()
        Assert.assertEquals(allSongBefore.size, mockData.size)
        iTunesDatabase.songDao().clearAll()
        val allSongAfter = iTunesDatabase.songDao().getAll()

        Assert.assertEquals(allSongAfter.size, 0)
    }

    @Test
    fun successResult_WhenSearchQuery() {

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
            ),
            Song(
                trackId = 3,
                collectionName = "as2",
                artistName = "as2",
                artworkUrl100 = "as2",
                trackPrice = 12.0,
                trackName = "lolo"
            )
        )
        iTunesDatabase.songDao().insertAll(mockData)
        val allSongBefore = iTunesDatabase.songDao().pagingSource("as")
        val result = (allSongBefore.create() as LimitOffsetDataSource).loadRange(0, 10)
        Assert.assertEquals(result.size, 2)
    }
}