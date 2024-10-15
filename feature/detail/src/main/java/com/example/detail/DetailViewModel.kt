package com.example.detail

import androidx.lifecycle.ViewModel
import com.example.domain.useCase.SaveLikeData
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val save: SaveLikeData) : ViewModel() {
}