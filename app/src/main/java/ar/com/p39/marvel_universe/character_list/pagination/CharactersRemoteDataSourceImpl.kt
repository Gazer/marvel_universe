package ar.com.p39.marvel_universe.character_list.pagination

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ar.com.p39.marvel_universe.network_models.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersRemoteDataSourceImpl @Inject constructor(
    private var charactersPagingSource: CharactersPagingSource
) : CharactersRemoteDataSource {
    override fun getCharacters(q: String?): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 1,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                charactersPagingSource.filter(q)
            }
        ).flow
    }
}