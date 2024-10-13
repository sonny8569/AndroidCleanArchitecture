package com.example.data.dataSoruce

import com.example.data.model.SearchDocument

interface NetworkDataSource {

    suspend fun image(
        query: String,
        page: Int,
        pageSize: Int,
        sort: String,
    ): Pair<List<SearchDocument.Document>, Boolean>

    suspend fun video(
        query: String,
        page: Int,
        pageSize: Int,
        sort: String,
    ): Pair<List<SearchDocument.Document>, Boolean>
}