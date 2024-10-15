package com.example.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder

object Serializar : KSerializer<SearchResult> {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = true
        coerceInputValues = true
    }
    override val descriptor: SerialDescriptor
        get() = PolymorphicSerializer(SearchResult::class).descriptor

    override fun deserialize(decoder: Decoder): SearchResult {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
        return json.decodeFromJsonElement(SearchResult.serializer(), jsonElement)
    }

    override fun serialize(encoder: Encoder, value: SearchResult) {
        return encoder.encodeSerializableValue(SearchResult.serializer(), value)
    }
}