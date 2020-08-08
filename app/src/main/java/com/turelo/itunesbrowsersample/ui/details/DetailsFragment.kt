package com.turelo.itunesbrowsersample.ui.details

import android.os.Bundle
import android.view.View
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.base.fragment.DataBoundAbstractFragment
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import com.turelo.itunesbrowsersample.databinding.DetailsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : DataBoundAbstractFragment<DetailsFragmentBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.details_fragment

    private val viewModel by viewModel<DetailsViewModel>()

    override fun viewModel(): BaseViewModel = viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireNotNull(viewModel)
    }

}