package ar.com.p39.marvel_universe.character_list.models

import ar.com.p39.marvel_universe.network_models.Character

data class CharactersResponse(
    val items: List<Character>,
    val count: Int,
    val limit: Int,
    val offset: Int,
    val total: Int,
)