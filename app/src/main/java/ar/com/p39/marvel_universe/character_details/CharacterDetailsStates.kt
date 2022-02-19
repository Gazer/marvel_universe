package ar.com.p39.marvel_universe.character_details

import ar.com.p39.marvel_universe.network_models.Character

sealed class CharacterDetailsStates {
    object Loading: CharacterDetailsStates()
    data class Loaded(val character: Character): CharacterDetailsStates()
    data class Error(val error: String): CharacterDetailsStates()
}
