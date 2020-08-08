package com.turelo.itunesbrowsersample.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.turelo.itunesbrowsersample.data.db.dao.SongDao
import com.turelo.itunesbrowsersample.models.Song

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class ITunesDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}