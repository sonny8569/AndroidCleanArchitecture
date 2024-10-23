package com.example.domain.repository

import com.example.data.dataSoruce.DeviceDataSource
import com.example.data.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(private val device: DeviceDataSource) :
    LikeRepository {
    private val key = "data"

    override suspend fun requestLikeInfo(): String? {
        return device.getData(key)
    }

    override suspend fun requestSaveData(data: String): Boolean {
        return device.saveData(data, key)
    }

    override suspend fun requestRemoveData(data: String): Boolean {
        return device.deleteData(data, key)
    }

}