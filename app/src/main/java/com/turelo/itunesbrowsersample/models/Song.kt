package com.turelo.itunesbrowsersample.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val trackId: Int,
    val trackName: String?,
    val collectionName: String?,
    val artistName: String?,
    val artworkUrl100: String = "",
    val trackPrice: Double
)