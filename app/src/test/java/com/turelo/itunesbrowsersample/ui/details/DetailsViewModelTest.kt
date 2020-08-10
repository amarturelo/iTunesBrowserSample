package com.turelo.itunesbrowsersample.ui.details

import com.turelo.itunesbrowsersample.common.AbstractViewModelTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest : AbstractViewModelTest() {

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setUp() {
        this.viewModel = DetailsViewModel(
            context
        )
    }

    @Test
    fun testPopulate_WhenCallWith() {
        this.viewModel.with(
            DetailsFragmentArgs(
                trackId = 12,
                artworkUrl100 = "artworkUrl100",
                artistName = "artistName",
                collectionName = "collectionName"
            )
        )

        Assert.assertEquals(this.viewModel.artistsNameLiveData().value, "artistName")
        Assert.assertEquals(this.viewModel.collectionNameLiveData().value, "collectionName")
        Assert.assertEquals(this.viewModel.artworkLiveData().value, "artworkUrl100")
    }
}