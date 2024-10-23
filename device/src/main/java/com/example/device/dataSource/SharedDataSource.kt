package com.example.device.dataSource

interface SharedDataSource {
    fun saveData(key: String, data: String): Boolean
    fun deleteData(key: String, data: String): Boolean
    fun getData(key : String): String?
}