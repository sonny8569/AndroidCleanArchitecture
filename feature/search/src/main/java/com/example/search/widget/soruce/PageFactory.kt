package com.example.search.widget.soruce

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource

internal object PageFactory {
    fun <Key : Any, Value : Any> create(pageSize: Int, enablePlaceholders: Boolean, pagingSource: PagingSource<Key, Value>) = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = enablePlaceholders
        ),
        pagingSourceFactory = { pagingSource }
    )
}