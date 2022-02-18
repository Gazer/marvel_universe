package ar.com.p39.marvel_universe.network_models

import com.squareup.moshi.Json

data class CharacterDataContainer(
    val count: Int,
    val limit: Int,
    val offset: Int,
    @field:Json(name = "results")
    val characters: List<Character>,
    val total: Int
)