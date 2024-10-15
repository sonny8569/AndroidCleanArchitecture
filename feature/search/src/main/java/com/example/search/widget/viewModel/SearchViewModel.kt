package com.example.search.widget.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.SearchResult
import com.example.domain.useCase.GetDeviceChangeData
import com.example.domain.useCase.SaveLikeData
import com.example.domain.useCase.SearchApi
import com.example.search.widget.soruce.PageFactory
import com.example.search.widget.soruce.SearchPagingSource
import com.example.search.widget.utill.KEY_SEARCH_QUERY
import com.example.search.widget.utill.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val search: SearchApi,
    private val save: SaveLikeData,
    private val getData: GetDeviceChangeData,
) : ViewModel() {
    private var _liveData = MutableLiveData<Action>()
    val liveData: LiveData<Action> get() = _liveData
    private val searchQuery: Flow<String?> =
        savedStateHandle.getStateFlow(key = KEY_SEARCH_QUERY, null)

    val items: Flow<PagingData<SearchResult>> = searchQuery
        .mapNotNull { it }
        .flatMapLatest { query -> createPager(query).flow }
        .cachedIn(viewModelScope)

    fun onSearch(query: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }


    fun onChangeLike(item: SearchResult) {
        viewModelScope.launch {
            when (val result = save.invoke(SaveLikeData.PARAM(item))) {
                is SaveLikeData.Result.Success -> {
                    _liveData.postValue(Action.SaveResult(result.item))
                }

                is SaveLikeData.Result.Fail -> {
                    _liveData.postValue(Action.Error(result.message))
                }
            }
        }
    }

    fun onCheckDataChange(list: List<SearchResult>) {
        if(list.isEmpty()){
            return
        }
        viewModelScope.launch {
            val data = getData.invoke(GetDeviceChangeData.PARAM(list))
            _liveData.postValue(Action.GetDeviceData(data.changeData))
        }
    }

    private fun createPager(query: String): Pager<SearchPagingSource.Param, SearchResult> =
        PageFactory.create(
            PAGE_SIZE,
            false,
            SearchPagingSource(query, search)
        )

    sealed interface Action {
        data class SaveResult(val data: SearchResult) : Action
        data class GetDeviceData(val data: List<Pair<Int, Boolean>>) : Action
        data class Error(val message: String) : Action
    }
}