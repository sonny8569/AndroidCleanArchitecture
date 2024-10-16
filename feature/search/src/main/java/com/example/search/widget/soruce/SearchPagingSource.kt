package com.example.search.widget.soruce

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.model.SearchResult
import com.example.domain.useCase.SearchApi

internal class SearchPagingSource(
    private val query: String,
    private val search: SearchApi,
) : PagingSource<SearchPagingSource.Param, SearchResult>() {


    data class Param(
        val page: Int = 1,
        val isImageEnd: Boolean = false,
        val isVideoEnd: Boolean = false,
    )

    override fun getRefreshKey(state: PagingState<Param, SearchResult>): Param? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.let { prevKey ->
                prevKey.copy(page = prevKey.page + 1)
            } ?: state.closestPageToPosition(it)?.nextKey?.let { nextKey ->
                nextKey.copy(page = nextKey.page - 1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Param>): LoadResult<Param, SearchResult> {
        return try {
            val param = params.key ?: Param()
            val response = search.invoke(
                SearchApi.PARAM(
                    query,
                    param.page,
                    param.isImageEnd,
                    param.isVideoEnd
                )
            )
            when (response) {
                is SearchApi.Result.Success -> mapResponse(
                    param,
                    response.data,
                    response.isImageEnd,
                    response.isVideoEnd
                )

                is SearchApi.Result.Fail ->{
                    Log.e("SearchPagingSource", "SearchApi.Result.Fail: No Response")
                    LoadResult.Error(Exception("No Response"))
                }
            }
        } catch (e: Exception) {
            Log.e("SearchPagingSource", "Exception :$e")
            LoadResult.Error(e)
        }
    }

    private fun mapResponse(
        param: Param,
        searchData: List<SearchResult>,
        isImageEnd: Boolean,
        isVideoEnd: Boolean,
    ): LoadResult.Page<Param, SearchResult> {
        return LoadResult.Page(
            data = searchData,
            prevKey = if (param.page == 1) null else param.copy(page = param.page - 1),
            nextKey = if (param.isImageEnd && param.isVideoEnd) {
                null
            } else {
                param.copy(
                    page = param.page + 1,
                    isImageEnd = isImageEnd,
                    isVideoEnd = isVideoEnd
                )
            }
        )
    }
}