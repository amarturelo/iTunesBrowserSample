package com.turelo.itunesbrowsersample.common

import androidx.lifecycle.Observer

class CaptureObserver<T> : Observer<T> {

    val capture: ArrayList<T> = arrayListOf()

    override fun onChanged(t: T) {
        this.capture.add(t)
    }


}