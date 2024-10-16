package com.example.domain.useCase

import com.example.data.dataSoruce.DeviceDataSource
import com.example.domain.UseCase
import com.example.domain.model.SearchResult
import com.example.domain.utill.DocumentConverter
import javax.inject.Inject

class GetDeviceChangeData @Inject constructor(private val deviceDataSource: DeviceDataSource) :
    UseCase<GetDeviceChangeData.PARAM, GetDeviceChangeData.Result> {
    data class PARAM(
        val data: List<SearchResult>,
    ) : UseCase.Param

    sealed interface Result : UseCase.Result {
        data class Success(val changeData: List<Pair<Int, Boolean>>) : Result
        data class Fail(val message: String) : Result
    }

    override suspend fun invoke(param: PARAM): Result {
        val deviceDataStr = deviceDataSource.getData()
        val device = deviceDataStr?.strToSearchResult() ?: emptyList<SearchResult>()
        if (device.isEmpty()) {
            return Result.Fail("save Data is Null")
        }
        return Result.Success(device.checkChangeLike(param.data))
    }

    private fun String.strToSearchResult(): List<SearchResult> {
        return DocumentConverter.fromJson(this)
    }

    private fun List<SearchResult>.checkChangeLike(data: List<SearchResult>): List<Pair<Int, Boolean>> {
        val deviceData = this.associateBy { it.id }
        val changedIndexes = mutableListOf<Pair<Int, Boolean>>()

        data.forEachIndexed { index, item ->
            val deviceItem = deviceData[item.id]
            if (deviceItem != null && item.isLike != deviceItem.isLike) {
                changedIndexes.add(Pair(index, deviceItem.isLike))
            }
        }
        return changedIndexes
    }

}