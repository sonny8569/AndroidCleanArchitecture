package com.example.domain.utill

import com.example.domain.model.SearchResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DocumentConverter {
    private val gson = Gson()

    fun toJson(documents: SearchResult): String {
        return gson.toJson(documents)
    }

    fun fromJson(json: String): ArrayList<SearchResult> {
        val type = object : TypeToken<ArrayList<SearchResult>>() {}.type
        return gson.fromJson(json, type)
    }
}