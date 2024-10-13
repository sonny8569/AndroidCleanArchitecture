package com.example.device

import com.example.data.dataSoruce.DeviceDataSource
import com.example.device.dataSource.SharedDataSource
import javax.inject.Inject

class DeviceController @Inject constructor(private val device: SharedDataSource) :
    DeviceDataSource {
    override suspend fun saveData(data: String, key: String): Boolean {
        return device.saveData(key, data)
    }

    override suspend fun deleteData(data: String, key: String): Boolean {
        return device.deleteData(key, data)
    }

    override suspend fun getData(): String? {
        return device.getData()
    }
}