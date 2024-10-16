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
        return Result.Success(data)
    }
}