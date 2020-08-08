package com.turelo.itunesbrowsersample.data.providers.response

import com.turelo.itunesbrowsersample.models.Song

data class SearchResponse(
    val resultCount: Int,
    val results: List<Song>
)