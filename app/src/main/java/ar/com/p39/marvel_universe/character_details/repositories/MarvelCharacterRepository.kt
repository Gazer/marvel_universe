package ar.com.p39.marvel_universe.character_details.repositories

import ar.com.p39.marvel_universe.character_details.models.toCharacter
import ar.com.p39.marvel_universe.common.Result
import ar.com.p39.marvel_universe.network.MarvelService
import ar.com.p39.marvel_universe.network_models.Character
import javax.inject.Inject

class MarvelCharacterRepository @Inject constructor(
    private val service: MarvelService
) {
    suspend fun getCharacter(characterId: String): Result<Character> {
        return try {
            val response = service.getCharacter(characterId)
            if (response.code == "200") {
                val result = response.toCharacter()
                Result.Success(result)
            } else {
                Result.Error(response.status)
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: e.toString())
        }
    }
}