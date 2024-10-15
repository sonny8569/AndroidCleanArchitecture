package com.example.domain.useCase

import com.example.data.dataSoruce.DeviceDataSource
import com.example.domain.model.SearchResult
import com.example.domain.utill.DocumentConverter
import javax.inject.Inject

class GetDeviceChangeData @Inject constructor(private val deviceDataSource: DeviceDataSource) {

    suspend fun invoke(param: PARAM): Result {
        val deviceDataStr = deviceDataSource.getData()
        val device = deviceDataStr?.strToSearchResult() ?: emptyList<SearchResult>()
        return Result(device.checkChangeLike(param.data))
    }

    private fun String.strToSearchResult(): List<SearchResult> {
        return DocumentConverter.fromJson(this)
    }

    private fun List<SearchResult>.checkChangeLike(data: List<SearchResult>): List<Pair<Int, Boolean>> {
        val deviceData = this.associateBy { it.id }
        val changedIndexes = mutableListOf<Pair<Int, Boolean>>()

        data.forEachIndexed { index, item ->
            deviceData[item.id]?.let { deviceItem ->
                if (item.isLike != deviceItem.isLike) {
                    item.isLike = deviceItem.isLike
                    changedIndexes.add(Pair(index, deviceItem.isLike))
                }
            }
        }

        return changedIndexes
    }

    data class PARAM(
        val data: List<SearchResult>,
    )

    data class Result(
        val changeData: List<Pair<Int, Boolean>>,
    )
}