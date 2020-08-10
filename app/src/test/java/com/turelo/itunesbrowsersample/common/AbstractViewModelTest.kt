package com.turelo.itunesbrowsersample.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.turelo.itunesbrowsersample.ITunesBrowserApp
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
abstract class AbstractViewModelTest {

    protected val lifecycle = LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))

    protected val lifecycleOwner:LifecycleOwner = Mockito.mock(LifecycleOwner::class.java).also {
        Mockito.`when`(it.lifecycle).thenReturn(lifecycle)
    }


    protected val context: ITunesBrowserApp =
        ApplicationProvider.getApplicationContext()

    @get:Rule
    val executeLiveDataInstantly = InstantTaskExecutorRule()

}