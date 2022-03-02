package ar.com.p39.marvel_universe.character_list.models

import ar.com.p39.marvel_universe.network_models.CharacterDataContainer

fun CharacterDataContainer.toCharactersResponse() = CharactersResponse(
    items = characters,
    count = count,
    limit = limit,
    offset = offset,
    total = total,
)