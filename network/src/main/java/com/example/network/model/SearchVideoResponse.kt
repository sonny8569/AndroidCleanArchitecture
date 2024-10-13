package com.example.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoResponse(
    val documents: List<Document> = emptyList(),
    val meta: Meta = Meta(),
) {

    @Serializable
    data class Document(
        val datetime: String = "",
        @SerialName("thumbnail")
        val thumbnailUrl: String = "",
        val title: String = "",
        val url: String = "",
        val collection: String = "video"
    )

    @Serializable
    data class Meta(
        @SerialName("is_end")
        val isEnd: Boolean = false,
    )
}