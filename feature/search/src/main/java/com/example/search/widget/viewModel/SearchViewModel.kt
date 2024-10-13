package com.example.search.widget.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SearchResult
import com.example.domain.useCase.SearchApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val search: SearchApiUseCase) : ViewModel() {
    private var _liveData = MutableLiveData<Action>()
    val liveData: LiveData<Action> get() = _liveData

    fun onSearch(query: String) {
        viewModelScope.launch {
            when (val result = search.invoke(SearchApiUseCase.PARAM(query))) {
                is SearchApiUseCase.Result.Success -> {
                    _liveData.postValue(Action.Search(result.data))
                }

                is SearchApiUseCase.Result.Fail -> {
                    _liveData.postValue(Action.Error(result.message))
                }
            }
        }
    }

    sealed interface Action {
        data class Search(val data: ArrayList<SearchResult>) : Action
        data class SaveResult(val message: String) : Action
        data class DeleteResult(val message: String) : Action
        data class Error(val message: String) : Action
    }
}