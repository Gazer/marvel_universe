package ar.com.p39.marvel_universe.character_list.use_cases

import ar.com.p39.marvel_universe.character_list.models.CharactersResponse
import ar.com.p39.marvel_universe.character_list.repositories.MarvelCharactersRepository
import ar.com.p39.marvel_universe.common.Result
import ar.com.p39.marvel_universe.network_models.Character
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import javax.inject.Inject

/*
 * Usuallla I do not like to have "proxy use-cases" because I think that they make no sense, but
 * here we can do some validation about the parameters or other stuff.
 */
class GetAllCharacters @Inject constructor(
    private val charactersRepository: MarvelCharactersRepository
) {
    suspend operator fun invoke(
        nameStartsWith: String?,
        limit: Int,
        offset: Int
    ): Result<CharactersResponse> {
        return charactersRepository.getCharacters(nameStartsWith, limit, offset)
    }
}