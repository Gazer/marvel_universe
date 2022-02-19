package ar.com.p39.marvel_universe.network

import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelService {
    @Headers("Content-Type:application/json; charset=UTF-8")
    @GET("public/characters")
    suspend fun getCharacters(@Query("limit") limit: Int, @Query("offset") offset: Int): CharacterDataWrapper

    @GET("public/characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: String): CharacterDataWrapper
}