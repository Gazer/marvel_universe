package ar.com.p39.marvel_universe.character_details.use_cases

import ar.com.p39.marvel_universe.character_details.repositories.MarvelCharacterRepository
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import javax.inject.Inject

class GetCharacter @Inject constructor(
    private val characterRepository: MarvelCharacterRepository
) {
    suspend operator fun invoke(characterId: String): CharacterDataWrapper {
        return characterRepository.getCharacter(characterId)
    }
}