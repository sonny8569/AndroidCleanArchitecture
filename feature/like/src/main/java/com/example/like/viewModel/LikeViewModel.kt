package com.example.like.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SearchResult
import com.example.domain.useCase.GetDeviceChangeData
import com.example.domain.useCase.GetDeviceData
import com.example.domain.useCase.SaveLikeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    private val getDeviceData: GetDeviceData,
    private val save: SaveLikeData,
    private val getData: GetDeviceChangeData,
) : ViewModel() {
    private var _liveData = MutableLiveData<Action>()
    val liveData: LiveData<Action> get() = _liveData

    private var _currentData = MutableLiveData<LikeData>()
    val currentData: LiveData<LikeData> get() = _currentData

    fun onGetData() {
        viewModelScope.launch {
            val currentList = _currentData.value?.likeData?.map { it } ?: emptyList()
            when(val result = getDeviceData.invoke(GetDeviceData.PARMA(currentList))){
                is GetDeviceData.Result.Success ->{
                    _currentData.postValue(LikeData(result.data))
                }
                is GetDeviceData.Result.Fail ->{
                    _liveData.postValue(Action.Error(result.message))
                }
            }
        }
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

    fun onClickItem(item: SearchResult) {
        _liveData.value = Action.OnClickItem(item)
    }

    data class LikeData(
        val likeData: List<SearchResult>,
    )

    sealed interface Action {
        data class SaveResult(val data: SearchResult) : Action
        data class OnClickItem(val data: SearchResult) : Action
        data class Error(val message: String) : Action
    }
}