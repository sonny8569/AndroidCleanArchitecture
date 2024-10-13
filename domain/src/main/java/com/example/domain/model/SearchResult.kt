package com.example.domain.model

data class SearchResult(
    val title: String,
    val url: String,
    val thumbNail: String,
    val dateTime: String,
    val collection: String,
    val type: String,
    var isLike: Boolean = false,
    val id: String,
)