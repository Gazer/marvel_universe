package ar.com.p39.marvel_universe.network_models

import com.squareup.moshi.Json

data class CharacterDataWrapper(
    val attributionHTML: String,
    val attributionText: String,
    val code: String,
    val copyright: String,
    @field:Json(name = "data")
    val characterData: CharacterDataContainer,
    val etag: String,
    val status: String
)