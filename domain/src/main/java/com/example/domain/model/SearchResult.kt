package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val title: String,
    val url: String,
    val thumbNail: String,
    val dateTime: String,
    val collection: String,
    val type: String,
    var isLike: Boolean = false,
    val id: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SearchResult) return false
        return id == other.thumbNail
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "SearchResult(title='$title', url='$url', thumbNail='$thumbNail', dateTime='$dateTime', collection='$collection', type='$type', isLike=$isLike, id='$id')"
    }
}