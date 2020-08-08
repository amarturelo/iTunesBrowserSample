package com.turelo.itunesbrowsersample.repositories.impl

import androidx.paging.DataSource
import com.turelo.itunesbrowsersample.data.db.dao.SongDao
import com.turelo.itunesbrowsersample.data.providers.ITunesProvider
import com.turelo.itunesbrowsersample.models.PagingStatus
import com.turelo.itunesbrowsersample.models.Song
import com.turelo.itunesbrowsersample.repositories.ITunesRepository
import io.reactivex.Completable
import io.reactivex.Single

class ITunesRepositoryImpl(
    private val iTunesProvider: ITunesProvider,
    private val songDao: SongDao
) : ITunesRepository {
    override fun search(term: String): DataSource.Factory<Int, Song> =
        songDao.pagingSource(term)

    override fun search(term: String, pagingStatus: PagingStatus): Single<PagingStatus> =
        iTunesProvider.search(term, pagingStatus.page, pagingStatus.limit)
            .flatMap { response ->
                val sources: ArrayList<Completable> = ArrayList()
                if (pagingStatus.page == 1) {
                    sources.add(clearCache())
                }
                sources.add(insertItemsIntoDb(response.results))
                return@flatMap Completable.concat(sources)
                    .andThen(
                        Single.just(
                            PagingStatus(
                                page = if (response.resultCount != 0) pagingStatus.page + 1 else pagingStatus.page,
                                next = response.resultCount != 0
                            )
                        )
                    )
            }

    private fun insertItemsIntoDb(songs: List<Song>): Completable = Completable.create {
        this.songDao.insertAll(songs)
        it.onComplete()
    }

    private fun clearCache(): Completable = Completable.create {
        this.songDao.clearAll()
        it.onComplete()
    }
}