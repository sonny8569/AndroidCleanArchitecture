package com.example.domain.useCase

import com.example.data.dataSoruce.DeviceDataSource
import com.example.data.model.SearchDocument
import com.example.data.repository.LikeRepository
import com.example.data.repository.SearchRepository
import com.example.domain.UseCase
import com.example.domain.model.SearchResult
import com.example.domain.utill.DocumentConverter
import javax.inject.Inject

class SearchApi @Inject constructor(
    private val searchRepository: SearchRepository,
    private val likeRepository: LikeRepository,
) : UseCase<SearchApi.Param, SearchApi.Result> {

    data class Param(
        val query: String,
        val page: Int,
        var isImageEnd: Boolean = false,
        var isVideoEnd: Boolean = false,
    ) : UseCase.Param

    sealed interface Result : UseCase.Result {
        data class Success(val data: ArrayList<SearchResult> , val isImageEnd: Boolean , val isVideoEnd : Boolean) : Result
        data class Fail(val message: String) : Result
    }

    override suspend fun invoke(param: Param): Result {
        if (param.query.isEmpty()) {
            return Result.Fail("query is empty")
        }
        if (param.isImageEnd && param.isVideoEnd) {
            return Result.Success(java.util.ArrayList(emptyList()),isImageEnd = param.isImageEnd,isVideoEnd = param.isVideoEnd)
        }
        val (imageData, isImageEnd) = try {
            loadImage(param.query, param.page)
        } catch (e: Exception) {
            emptyList<SearchResult>() to false
        }
        val (videoData, isVideoEnd) = try {
            loadVideo(param.query, param.page)
        } catch (e: Exception) {
            emptyList<SearchResult>() to false
        }
        val feeds = (imageData + videoData).sortedByDescending { it.dateTime }
        if (feeds.isEmpty() && isImageEnd && isVideoEnd) {
            return Result.Fail("Load Error")
        }
        val likeDataStr = likeRepository.requestLikeInfo()
        if (likeDataStr == null || likeDataStr == "") {
            return Result.Success(java.util.ArrayList(feeds), isImageEnd, isVideoEnd)
        }
        val likeData = DocumentConverter.fromJson(likeDataStr)
        val result = makeLikeData(feeds, likeData)
        return Result.Success(java.util.ArrayList(result), isImageEnd, isVideoEnd)
    }

    private suspend fun loadImage(
        query: String,
        page: Int,
    ): Pair<List<SearchResult>, Boolean> {
        return try {
            val (response, isEnd) = searchRepository.requestSearchImage(query, page)
            val documents = response.map { it.toSearchResult() }
            Pair(documents, isEnd)
        } catch (e: Exception) {
            Pair(emptyList(), false)
        }
    }

    private suspend fun loadVideo(
        query: String,
        page: Int,
    ): Pair<List<SearchResult>, Boolean> {
        return try {
            val (response, isEnd) = searchRepository.requestSearchVideo(query, page)
            val documents = response.map { it.toSearchResult() }
            Pair(documents, isEnd)
        } catch (e: Exception) {
            Pair(emptyList(), false)
        }
    }

    private fun makeLikeData(
        data: List<SearchResult>,
        likeData: List<SearchResult>,
    ): List<SearchResult> {
        val likeDataSet: Set<String> = likeData.map { it.id }.toHashSet()
        data.forEach { document ->
            if (likeDataSet.contains(document.id)) {
                document.isLike = true
            }
        }
        return data
    }

    private fun SearchDocument.Document.toSearchResult(): SearchResult {
        return SearchResult(
            title = this.title,
            url = this.url,
            thumbNail = this.thumbNail,
            dateTime = this.dateTime,
            collection = this.collection,
            type = this.type,
            id = this.id
        )
    }
}