package com.turelo.itunesbrowsersample.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.turelo.itunesbrowsersample.ITunesBrowserApp
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class AbstractViewModelTest {

    protected val context: ITunesBrowserApp =
        ApplicationProvider.getApplicationContext()

    @get:Rule
    val executeLiveDataInstantly = InstantTaskExecutorRule()
}