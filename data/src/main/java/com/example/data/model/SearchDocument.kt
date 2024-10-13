package com.example.data.model


data class SearchDocument(
    val document: List<Document> = emptyList(),
    val meta: Meta = Meta(),
) {
    data class Document(
        val title: String,
        val url: String,
        val thumbNail: String,
        val dateTime: String,
        val collection: String,
        val type: String,
        val id: String,
    )

    data class Meta(
        val isEnd: Boolean = false,
    )
}