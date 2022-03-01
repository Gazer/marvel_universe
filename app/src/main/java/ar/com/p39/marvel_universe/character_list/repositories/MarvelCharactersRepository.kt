package ar.com.p39.marvel_universe.character_list.repositories

import ar.com.p39.marvel_universe.network.MarvelService
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import javax.inject.Inject

/*
 * This app does not have any extra logic in the repository, but on other apps we can save data in
 * the local database, or convert the network model into a domain model so our app and our API are
 * not highly couple.
 */
class MarvelCharactersRepository @Inject constructor(
    private val service: MarvelService
) {
    suspend fun getCharacters(nameStartsWith: String?, limit: Int, offset: Int): CharacterDataWrapper {
        return service.getCharacters(nameStartsWith, limit, offset)
    }
}