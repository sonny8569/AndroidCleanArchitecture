package com.example.data.dataSoruce

interface DeviceDataSource {
    suspend fun saveData(data: String, key: String): Boolean
    suspend fun deleteData(data: String , key : String): Boolean
    suspend fun getData(): String?
}