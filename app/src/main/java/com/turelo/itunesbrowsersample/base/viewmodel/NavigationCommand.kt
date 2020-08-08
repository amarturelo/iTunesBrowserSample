package com.turelo.itunesbrowsersample.base.viewmodel

import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

sealed class NavigationCommand {
    data class To(
        val directions: NavDirections,
        val extras: FragmentNavigator.Extras = FragmentNavigator.Extras.Builder().build()
    ) :
        NavigationCommand()

    object Back : NavigationCommand()
    data class BackTo(val destinationId: Int) : NavigationCommand()
    object ToRoot : NavigationCommand()
}