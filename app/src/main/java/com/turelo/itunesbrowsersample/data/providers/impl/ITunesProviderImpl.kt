package com.turelo.itunesbrowsersample.data.providers.impl

import com.turelo.itunesbrowsersample.data.providers.ITunesProvider
import com.turelo.itunesbrowsersample.data.providers.RestProvider
import com.turelo.itunesbrowsersample.data.providers.response.SearchResponse
import com.turelo.itunesbrowsersample.data.providers.services.ITunesService
import io.reactivex.Single

class ITunesProviderImpl : RestProvider<ITunesService>(ITunesService::class.java), ITunesProvider {
    override fun search(term: String, page: Int, limit: Int): Single<SearchResponse> =
        mApi.search(term, page, limit)
}