package com.turelo.itunesbrowsersample.ui.browser.models

data class SongItemViewModel(
    val trackId: Int,
    val trackName: String,
    val collectionName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackPrice: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongItemViewModel

        if (trackId != other.trackId) return false
        if (trackName != other.trackName) return false
        if (collectionName != other.collectionName) return false
        if (artistName != other.artistName) return false
        if (artworkUrl100 != other.artworkUrl100) return false
        if (trackPrice != other.trackPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trackId
        result = 31 * result + trackName.hashCode()
        result = 31 * result + collectionName.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + artworkUrl100.hashCode()
        result = 31 * result + trackPrice.hashCode()
        return result
    }

    override fun toString(): String {
        return "SongItemViewModel(trackId=$trackId, trackName='$trackName')"
    }


}