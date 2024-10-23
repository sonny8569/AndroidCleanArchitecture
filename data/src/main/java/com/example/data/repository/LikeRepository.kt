package com.example.data.repository

interface LikeRepository {
    suspend fun requestLikeInfo() : String?
    suspend fun requestSaveData(data : String) : Boolean
    suspend fun requestRemoveData(data : String) : Boolean
}