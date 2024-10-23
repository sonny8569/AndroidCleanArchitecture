package com.example.data.repository

import android.app.appsearch.SearchResult
import com.example.data.model.SearchDocument

interface SearchRepository {
    suspend fun requestSearchImage(query : String , page : Int) : Pair<List<SearchDocument.Document> , Boolean>

    suspend fun requestSearchVideo(query: String , page: Int) : Pair<List<SearchDocument.Document> , Boolean>
}