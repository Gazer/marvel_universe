package ar.com.p39.marvel_universe.character_list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ar.com.p39.marvel_universe.network_models.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersRemoteDataSource @Inject constructor(
    private var charactersPagingSource: CharactersPagingSource
) {
    fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = true),
            pagingSourceFactory = {
                charactersPagingSource
            }
        ).flow
    }
}