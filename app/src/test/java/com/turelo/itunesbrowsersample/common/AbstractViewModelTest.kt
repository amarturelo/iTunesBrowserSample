package com.turelo.itunesbrowsersample.common

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.turelo.itunesbrowsersample.ITunesBrowserApp
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class AbstractViewModelTest {

    val context = ApplicationProvider.getApplicationContext<ITunesBrowserApp>()

    @get:Rule
    val executeLiveDataInstantly = InstantTaskExecutorRule()
}