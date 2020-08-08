package com.turelo.itunesbrowsersample.ui.common

import android.view.View

interface CellClickListener<T> {
    fun onCellClickListener(data: T, view: View)
}