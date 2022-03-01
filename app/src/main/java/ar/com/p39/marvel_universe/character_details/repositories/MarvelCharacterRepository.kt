package ar.com.p39.marvel_universe.character_details.repositories

import ar.com.p39.marvel_universe.network.MarvelService
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import javax.inject.Inject

class MarvelCharacterRepository@Inject constructor(
    private val service: MarvelService
) {
    suspend fun getCharacter( characterId: String): CharacterDataWrapper {
        return service.getCharacter(characterId)
    }
}