package com.example.network

import com.example.data.dataSoruce.NetworkDataSource
import com.example.data.model.SearchDocument
import com.example.network.api.SearchApi
import com.example.network.model.SearchImageResponse
import com.example.network.model.SearchVideoResponse
import javax.inject.Inject

class NetworkController @Inject constructor(private val api: SearchApi) : NetworkDataSource {

    override suspend fun image(
        query: String,
        page: Int,
        pageSize: Int,
        sort: String,
    ): Pair<List<SearchDocument.Document>, Boolean> {
        val response = api.searchImage(query, page, sort, pageSize)
        val documents = response.documents.map { doc -> doc.toSearchDocument() }
        val isEnd = response.meta.isEnd // meta는 단일 객체에서 가져옴
        return Pair(documents, isEnd)
    }

    override suspend fun video(
        query: String,
        page: Int,
        pageSize: Int,
        sort: String,
    ): Pair<List<SearchDocument.Document>, Boolean> {
        val response = api.searchVideo(query, page, sort, pageSize)
        val documents = response.documents.map { doc -> doc.toSearchDocument() }
        val isEnd = response.meta.isEnd
        return Pair(documents, isEnd)
    }

    private fun SearchImageResponse.Document.toSearchDocument(): SearchDocument.Document {
        return SearchDocument.Document(
            title = this.title,
            url = this.url,
            thumbNail = this.thumbnailUrl,
            dateTime = this.datetime,
            collection = this.collection,
            type = "image",
            id = this.thumbnailUrl
        )
    }

    private fun SearchVideoResponse.Document.toSearchDocument(): SearchDocument.Document {
        return SearchDocument.Document(
            title = this.title,
            url = this.url,
            thumbNail = this.thumbnailUrl,
            dateTime = this.datetime,
            collection = this.collection,
            type = "video",
            id = this.thumbnailUrl
        )
    }
}