package com.turelo.itunesbrowsersample.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.turelo.itunesbrowsersample.models.Song

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(songs: List<Song>)

    @Query("SELECT * FROM songs WHERE trackName LIKE '%' || :query || '%'")
    fun pagingSource(query: String): DataSource.Factory<Int, Song>

    @Query("DELETE FROM songs")
    fun clearAll(): Int

    @Query("SELECT * FROM songs")
    fun getAll(): List<Song>
}