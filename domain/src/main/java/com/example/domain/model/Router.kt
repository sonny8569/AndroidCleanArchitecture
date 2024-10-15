package com.example.domain.model

interface Router {

    fun navigateToDetail(data: SearchResult, isLike: Boolean)

    companion object {
        const val KEY_SEARCH_RESULT = "KEY_SEARCH_RESULT"
        const val KEY_IS_LIKE = "KEY_IS_LIKE"
    }
}