package com.turelo.itunesbrowsersample.models

data class PagingStatus(val page: Int = 1, val limit: Int = 20, val next: Boolean = true) {
    override fun toString(): String {
        return "PagingStatus(page=$page, limit=$limit, next=$next)"
    }
}