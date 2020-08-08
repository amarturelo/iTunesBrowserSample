package com.turelo.itunesbrowsersample.data.providers

import com.turelo.itunesbrowsersample.data.providers.response.SearchResponse
import io.reactivex.Single

interface ITunesProvider {
    fun search(
        term: String,
        page: Int,
        limit: Int
    ): Single<SearchResponse>
}