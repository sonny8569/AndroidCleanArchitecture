package com.example.domain.useCase

import com.example.data.dataSoruce.DeviceDataSource
import com.example.domain.UseCase
import com.example.domain.model.SearchResult
import com.example.domain.utill.DocumentConverter
import javax.inject.Inject

class GetDeviceData @Inject constructor(private val deviceDataSource: DeviceDataSource) :
    UseCase<GetDeviceData.PARMA, GetDeviceData.Result> {
    sealed interface Result : UseCase.Result {
        data class Success(val data: List<SearchResult>) : Result
        data class Fail(val message: String) : Result
    }

    data class PARMA(val currentData: List<SearchResult>) : UseCase.Param

    override suspend fun invoke(param: PARMA): Result {
        val deviceStr = deviceDataSource.getData()
        if (deviceStr == null || deviceStr == "") {
            return Result.Fail("No data")
        }
        val data = DocumentConverter.fromJson(deviceStr)
        if (param.currentData.isEmpty()) {
            return Result.Success(data)
        }
        return Result.Success(param.currentData.changeData(data))
    }

    private fun List<SearchResult>.changeData(deviceData: List<SearchResult>): List<SearchResult> {
        val deviceDataIds = deviceData.map { it.id }.toSet()
        return this.filter { it.id in deviceDataIds }
    }
}