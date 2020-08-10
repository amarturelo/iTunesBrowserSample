package com.turelo.itunesbrowsersample.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.common.AbstractBaseTest
import org.junit.Before
import org.junit.Test

class DetailsFragmentTest : AbstractBaseTest() {

    private lateinit var fragmentScenario: FragmentScenario<DetailsFragment>
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        val fragmentArgs = Bundle().apply {
            this.putString("artworkUrl100", "artworkUrl100")
            this.putString("collectionName", "collectionName")
            this.putString("artistName", "artistName")
            this.putInt("trackId", 2)
        }

        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        fragmentScenario =
            launchFragmentInContainer<DetailsFragment>(factory = object : FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                    return DetailsFragment().also { fragment ->
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
            }, themeResId = R.style.AppTheme_NoActionBar, fragmentArgs = fragmentArgs)
    }

    @Test
    fun updateUI_WhenStart() {
        onView(withId(R.id.collectionName))
            .check(matches(withText("collectionName")))
        onView(withId(R.id.bandName))
            .check(matches(withText("artistName")))
        onView(withId(R.id.artwork))
            .check(matches(isDisplayed()))
    }
}