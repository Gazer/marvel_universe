package ar.com.p39.marvel_universe.character_list

import androidx.paging.PagingData
import ar.com.p39.marvel_universe.network_models.Character
import kotlinx.coroutines.flow.Flow

interface CharactersRemoteDataSource  {
    fun getCharacters(q: String?): Flow<PagingData<Character>>
}