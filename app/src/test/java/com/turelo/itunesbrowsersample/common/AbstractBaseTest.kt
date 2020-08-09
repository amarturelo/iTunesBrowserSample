package com.turelo.itunesbrowsersample.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
abstract class AbstractBaseTest {

    @get:Rule
    val executeLiveDataInstantly: TestRule = InstantTaskExecutorRule()

}