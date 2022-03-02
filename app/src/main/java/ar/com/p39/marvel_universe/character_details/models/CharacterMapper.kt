package ar.com.p39.marvel_universe.character_details.models

import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper

fun CharacterDataWrapper.toCharacter() = characterData.characters.first()