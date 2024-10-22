package com.example.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Router
import com.example.domain.model.SearchResult
import com.example.domain.useCase.SaveLikeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val save: SaveLikeData,
    private val saveStateHandle: SavedStateHandle,
) : ViewModel() {

    private val searchData: SearchResult =
        saveStateHandle.get<String>(Router.KEY_SEARCH_RESULT)?.let {
            Json.decodeFromString(SearchResult.serializer(), it)
        } ?: throw IllegalArgumentException("Search Result is required")

    private val isLike: Boolean = saveStateHandle[Router.KEY_IS_LIKE] ?: false

    private val _liveData: MutableLiveData<Action> = MutableLiveData()
    val livedata: LiveData<Action> get() = _liveData

    init {
        _liveData.value = Action(searchData, isLike)
    }

    fun onLikeChange() {
        val currentData = _liveData.value ?: return
        val changed = currentData.copy(isLike = !currentData.isLike)
        _liveData.value = changed
        viewModelScope.launch {
            save.invoke(SaveLikeData.Param(changed.data.copy(isLike = changed.isLike)))
        }
        saveStateHandle[Router.KEY_IS_LIKE] = changed.isLike
    }

    data class Action(
        val data: SearchResult,
        val isLike: Boolean,
    )
}