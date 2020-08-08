package com.turelo.itunesbrowsersample.ui.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.base.fragment.DataBoundAbstractFragment
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import com.turelo.itunesbrowsersample.databinding.DetailsFragmentBinding
import kotlinx.android.synthetic.main.details_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : DataBoundAbstractFragment<DetailsFragmentBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.details_fragment

    private val viewModel by viewModel<DetailsViewModel>()

    private val args: DetailsFragmentArgs by navArgs()

    override fun viewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        artwork.transitionName = "artwork${args.trackId}"
        collectionName.transitionName = "collectionName${args.trackId}"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewModel.with(
            args
        )
        this.setupNavigationBar()
        this.setupObservers()

    }

    private fun setupNavigationBar() {
        NavigationUI.setupWithNavController(toolbar, findNavController())
    }

    private fun setupObservers() {
        this.viewModel.artworkLiveData()
            .observe(viewLifecycleOwner, Observer {
                Glide.with(artwork)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .into(artwork)
            })
    }

}