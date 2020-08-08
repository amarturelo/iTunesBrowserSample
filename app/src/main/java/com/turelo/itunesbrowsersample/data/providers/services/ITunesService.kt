package com.turelo.itunesbrowsersample.data.providers.services

import com.turelo.itunesbrowsersample.data.providers.response.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {
    @GET("search")
    fun search(
        @Query("term") term: String,
        @Query("offset") page: Int,
        @Query("limit") limit: Int
    ): Single<SearchResponse>
}