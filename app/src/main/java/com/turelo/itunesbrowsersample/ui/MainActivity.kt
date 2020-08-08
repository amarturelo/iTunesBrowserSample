package com.turelo.itunesbrowsersample.ui

import androidx.navigation.Navigation.findNavController
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.base.activity.BaseActivity

class MainActivity : BaseActivity() {
    override val layoutResId: Int =
        R.layout.activity_main

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.navHostFragment).navigateUp()
}