package com.turelo.itunesbrowsersample.ui.browser

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.doOnEnd
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.base.fragment.DataBoundAbstractFragment
import com.turelo.itunesbrowsersample.base.viewmodel.BaseViewModel
import com.turelo.itunesbrowsersample.databinding.BrowserFragmentBinding
import com.turelo.itunesbrowsersample.extensions.rxFocus
import com.turelo.itunesbrowsersample.utils.KeyboardManager
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.browser_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowserFragment : DataBoundAbstractFragment<BrowserFragmentBinding>() {

    override val layoutResourceId: Int = R.layout.browser_fragment

    override fun viewModel(): BaseViewModel = viewModel

    private val viewModel by viewModel<BrowserViewModel>()

    private lateinit var keyboardManager: KeyboardManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tag.d("onViewCreated")
        binding.viewModel = viewModel

        searchView.rxFocus()
            .flatMap {
                if (it) {
                    return@flatMap this.collapse().toObservable<Boolean>()
                } else {
                    return@flatMap this.expanded().toObservable<Boolean>()
                }
            }
            .subscribe()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireNotNull(viewModel)
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

}