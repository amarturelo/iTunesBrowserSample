package com.turelo.itunesbrowsersample.ui.browser

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.turelo.itunesbrowsersample.R

class BrowserFragment : Fragment() {

    companion object {
        fun newInstance() = BrowserFragment()
    }

    private lateinit var viewModel: BrowserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.browser_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BrowserViewModel::class.java)
        // TODO: Use the ViewModel
    }

}