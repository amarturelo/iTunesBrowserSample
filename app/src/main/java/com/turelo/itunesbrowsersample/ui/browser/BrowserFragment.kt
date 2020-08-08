package com.turelo.itunesbrowsersample.ui.browser

import android.os.Bundle
import android.view.View
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.base.fragment.DataBoundAbstractFragment
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import com.turelo.itunesbrowsersample.databinding.BrowserFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowserFragment : DataBoundAbstractFragment<BrowserFragmentBinding>() {

    override fun viewModel(): BaseViewModel = viewModel

    private val viewModel by viewModel<BrowserViewModel>()

    override val layoutResourceId: Int = R.layout.browser_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tag.d("onViewCreated")
        binding.viewModel = viewModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireNotNull(viewModel)
    }

}