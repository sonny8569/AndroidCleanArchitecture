package com.example.domain.repository

import android.app.appsearch.SearchResult
import com.example.data.dataSoruce.NetworkDataSource
import com.example.data.model.SearchDocument
import com.example.data.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val network: NetworkDataSource) :
    SearchRepository {
    private val pageSize = 10
    private val sort = "recency"
    override suspend fun requestSearchImage(
        query: String,
        page: Int,
    ): Pair<List<SearchDocument.Document>, Boolean> {
        return network.image(query, page, pageSize, sort)
    }

    override suspend fun requestSearchVideo(
        query: String,
        page: Int,
    ): Pair<List<SearchDocument.Document>, Boolean> {
        return network.video(query, page, pageSize, sort)
    }

}