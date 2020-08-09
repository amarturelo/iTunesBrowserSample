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

fun SearchView.rxQuery(): Observable<String> {
    return Observable.create { emitter ->
        this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                emitter.onNext(newText ?: "")
                return true
            }
        })
    }
}