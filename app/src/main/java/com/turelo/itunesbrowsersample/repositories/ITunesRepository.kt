package com.turelo.itunesbrowsersample.repositories

import androidx.paging.DataSource
import com.turelo.itunesbrowsersample.models.PagingStatus
import com.turelo.itunesbrowsersample.models.Song
import io.reactivex.Single

interface ITunesRepository {
    fun search(term: String): DataSource.Factory<Int, Song>

    fun search(term: String, pagingStatus: PagingStatus): Single<PagingStatus>
}