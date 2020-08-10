package com.turelo.itunesbrowsersample.ui.browser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.common.AbstractBaseTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class BrowserFragmentTest : AbstractBaseTest() {

    private lateinit var fragmentScenario: FragmentScenario<BrowserFragment>
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        fragmentScenario =
            launchFragmentInContainer<BrowserFragment>(factory = object : FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                    return BrowserFragment().also { fragment ->
                        fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                            if (viewLifecycleOwner != null) {
                                Navigation.setViewNavController(
                                    fragment.requireView(),
                                    navController
                                )
                            }
                        }

                    }
                }
            }, themeResId = R.style.AppTheme_NoActionBar)
    }

    @Test
    fun test() {
        assertNotNull(fragmentScenario)
    }
}