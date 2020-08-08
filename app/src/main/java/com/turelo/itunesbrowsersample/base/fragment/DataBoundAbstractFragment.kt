package com.turelo.itunesbrowsersample.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import com.turelo.itunesbrowsersample.base.viewmodel.NavigationCommand
import timber.log.Timber

abstract class DataBoundAbstractFragment<T : ViewDataBinding> : Fragment() {

    protected val tag = Timber.tag(javaClass.simpleName)

    protected lateinit var binding: T

    abstract fun viewModel(): BaseViewModel

    @get:LayoutRes
    protected abstract val layoutResourceId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tag.d("onCreateView")
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tag.d("onActivityCreated")
        viewModel().navigationCommands.observe(viewLifecycleOwner, Observer { command ->
            when (command) {
                is NavigationCommand.To -> findNavController().navigate(command.directions)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.BackTo -> findNavController().popBackStack(
                    command.destinationId,
                    false
                )
                is NavigationCommand.ToRoot -> findNavController().popBackStack(
                    findNavController().graph.startDestination,
                    false
                )
            }
        })
    }
}