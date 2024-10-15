package com.example.domain.utill

import com.example.domain.model.SearchResult
import com.example.domain.useCase.SaveLikeData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DocumentConverter {
    private val gson = Gson()

    fun toJson(saveData: List<SearchResult>): String {
        return gson.toJson(saveData)
    }

    fun fromJson(json: String): List<SearchResult> {
        val type = object : TypeToken<List<SearchResult>>() {}.type
        return gson.fromJson(json, type)
    }
}