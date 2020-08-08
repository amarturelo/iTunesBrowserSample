package com.turelo.itunesbrowsersample.extensions

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable

fun SearchView.rxFocus(): Observable<Boolean> {
    return Observable.create<Boolean> { e ->
        this.setOnQueryTextFocusChangeListener { _, b ->
            e.onNext(b)
        }
    }
}