package com.example.domain.utill

import com.example.domain.model.SearchResult
import com.example.domain.useCase.SaveLikeData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DocumentConverter {
    private val gson = Gson()

    fun toJson(saveData: List<SaveLikeData.SaveData>): String {
        return gson.toJson(saveData)
    }

    fun fromJson(json: String): List<SaveLikeData.SaveData> {
        val type = object : TypeToken<List<SaveLikeData.SaveData>>() {}.type
        return gson.fromJson(json, type)
    }
}