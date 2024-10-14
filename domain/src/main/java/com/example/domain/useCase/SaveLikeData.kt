package com.example.domain.useCase

import android.util.Log
import com.example.data.dataSoruce.DeviceDataSource
import com.example.domain.UseCase
import com.example.domain.model.SearchResult
import com.example.domain.utill.DocumentConverter
import javax.inject.Inject

class SaveLikeData @Inject constructor(private val deviceDataSource: DeviceDataSource) :
    UseCase<SaveLikeData.PARAM, SaveLikeData.Result> {

    private val key: String = "dataAll"

    data class PARAM(val data: SearchResult) : UseCase.Param

    sealed interface Result : UseCase.Result {
        data class Success(val message: SearchResult) : Result
        data class Fail(val message: String) : Result
    }

    override suspend fun invoke(param: PARAM): Result {
        Log.d(javaClass.toString(), param.data.toString())
        return if (param.data.isLike) {
            handleLike(param.data)
        } else {
            handleUnlike(param.data)
        }
    }

    private suspend fun handleLike(data: SearchResult): Result {
        val deviceDataJson = deviceDataSource.getData()
        val saveObject = SaveData(data.id, data.isLike)

        return if (deviceDataJson.isNullOrEmpty()) {
            saveNewData(listOf(saveObject), data)
        } else {
            val deviceData = DocumentConverter.fromJson(deviceDataJson)
            val updatedDeviceData = updateDeviceData(deviceData, saveObject)
            saveUpdatedData(updatedDeviceData, data)
        }
    }

    private suspend fun handleUnlike(data: SearchResult): Result {
        val deviceDataJson = deviceDataSource.getData()
        val deviceData: List<SaveData> = if (deviceDataJson.isNullOrEmpty()) {
            emptyList()
        } else {
            DocumentConverter.fromJson(deviceDataJson)
        }

        val updatedDeviceData = deviceData.filter { it.id != data.id }
        return saveUpdatedData(updatedDeviceData, data)
    }

    private suspend fun saveNewData(data: List<SaveData>, searchResult: SearchResult): Result {
        val result = deviceDataSource.saveData(DocumentConverter.toJson(data), key)
        return if (result) {
            Result.Success(searchResult)
        } else {
            Result.Fail("save Fail")
        }
    }

    private suspend fun saveUpdatedData(data: List<SaveData>, searchResult: SearchResult): Result {
        val result = deviceDataSource.saveData(DocumentConverter.toJson(data), key)
        return if (result) {
            Result.Success(searchResult)
        } else {
            Result.Fail("save Fail")
        }
    }

    private fun updateDeviceData(deviceData: List<SaveData>, saveObject: SaveData): List<SaveData> {
        return if (deviceData.any { it.id == saveObject.id }) {
            deviceData.map {
                if (it.id == saveObject.id) it.copy(isLike = true) else it
            }
        } else {
            deviceData + saveObject.copy(isLike = true)
        }
    }

    data class SaveData(
        val id: String,
        val isLike: Boolean
    )
}