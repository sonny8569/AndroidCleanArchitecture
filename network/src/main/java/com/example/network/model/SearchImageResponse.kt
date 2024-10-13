package com.example.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchImageResponse(
    val documents: List<Document> = emptyList(),
    val meta: Meta = Meta()
) {
    @Serializable
    data class Document(
        val collection: String = "",
        val datetime: String = "",
        @SerialName("doc_url")
        val url: String = "",
        @SerialName("display_sitename")
        val title: String = "",
        @SerialName("thumbnail_url")
        val thumbnailUrl: String = "",
    )

    @Serializable
    data class Meta(
        @SerialName("is_end")
        val isEnd: Boolean = false,
    )

}