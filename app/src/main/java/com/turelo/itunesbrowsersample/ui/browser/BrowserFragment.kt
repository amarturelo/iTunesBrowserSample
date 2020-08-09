package com.turelo.itunesbrowsersample.ui.browser

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindUntilEvent
import com.turelo.itunesbrowsersample.AppExecutors
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.base.fragment.DataBoundAbstractFragment
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import com.turelo.itunesbrowsersample.databinding.BrowserFragmentBinding
import com.turelo.itunesbrowsersample.extensions.rxFocus
import com.turelo.itunesbrowsersample.extensions.setDivider
import com.turelo.itunesbrowsersample.ui.browser.BrowserViewModel.Companion.STATE_EMPTY_RESULT
import com.turelo.itunesbrowsersample.ui.browser.models.SongItemViewModel
import com.turelo.itunesbrowsersample.ui.common.CellClickListener
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kotlinx.android.synthetic.main.browser_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class BrowserFragment : DataBoundAbstractFragment<BrowserFragmentBinding>(),
    CellClickListener<SongItemViewModel> {

    override val layoutResourceId: Int = R.layout.browser_fragment

    override fun viewModel(): BaseViewModel = viewModel

    private val adapter = SongResultAdapter(listener = this)

    private val viewModel by viewModel<BrowserViewModel>()

    private val executors: AppExecutors by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tag.d("onViewCreated")
        binding.viewModel = viewModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.setupObservers()
        this.setupStatefulLayout()
        this.setupSearchView()
        this.setupRecyclerView()
    }

    private fun setupObservers() {
        searchView.rxFocus()
            .doOnNext {
                tag.d("Marturelo rxFocus: $it")
            }
            .debounce(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .flatMapCompletable {
                return@flatMapCompletable if (it) {
                    this.collapse()
                } else {
                    this.expanded()
                }
            }
            .andThen(io.reactivex.Observable.just(true))
            .bindUntilEvent<Boolean?>(this, Lifecycle.Event.ON_PAUSE)
            .subscribe()

        viewModel.isLoadingLiveData().observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = it
        })

        viewModel.songListLiveData.observe(viewLifecycleOwner, Observer {
            this.adapter.submitList(it)
        })

        viewModel.errorLiveData().observe(viewLifecycleOwner, Observer { retryAction ->
            view?.let { v ->
                Snackbar.make(v, "Something went terribly wrong", Snackbar.LENGTH_LONG)
                    .setAction("Try again") {
                        retryAction.retry.invoke()
                    }.show()
            }
        })
    }

    private fun setupSearchView() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                this@BrowserFragment.viewModel.search(newText ?: "")
                return true
            }

        })
    }

    private fun setupRecyclerView() {
        this.searchRV.adapter = this.adapter
        this.searchRV.setDivider(R.drawable.recycler_view_divider)

        this.searchRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?

                linearLayoutManager?.also { _ ->
                    tag.d("${linearLayoutManager.findLastCompletelyVisibleItemPosition()}")
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() >= adapter.itemCount - 6 && adapter.itemCount >= 20) {
                        this@BrowserFragment.viewModel.loadMore()
                    }
                }
            }
        })

    }

    private fun setupStatefulLayout() {
        statefulLayout.setStateView(
            STATE_EMPTY_RESULT,
            LayoutInflater.from(requireContext()).inflate(
                R.layout.state_list_empty_result,
                null
            )
        )
    }

    private fun collapse(): Completable = Completable.create { emitter ->
        val anim: ValueAnimator =
            ValueAnimator.ofInt(app_bar.measuredHeight, app_bar.measuredHeight - 112)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                app_bar.layoutParams
            layoutParams.height = value
            app_bar.layoutParams = layoutParams
        }
        anim.duration = 200
        anim.doOnEnd {
            emitter.onComplete()
        }

        toolbar.animate()
            .alpha(0f)
            .setDuration(100)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    toolbar.visibility = View.GONE
                }
            })

        anim.start()
    }

    private fun expanded(): Completable = Completable.create { emitter ->
        val anim: ValueAnimator =
            ValueAnimator.ofInt(app_bar.measuredHeight, app_bar.measuredHeight + 112)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                app_bar.layoutParams
            layoutParams.height = value
            app_bar.layoutParams = layoutParams
        }
        anim.duration = 200
        anim.doOnEnd {
            emitter.onComplete()
        }

        toolbar.alpha = 0f
        toolbar.visibility = View.VISIBLE

        toolbar.animate()
            .alpha(1f)
            .setDuration(100)
            .setListener(null)

        anim.start()
    }

    override fun onCellClickListener(data: SongItemViewModel, view: View) {
        tag.d("onCellClickListener: $data")
        this.viewModel.itemSelect(data, view)
    }

}