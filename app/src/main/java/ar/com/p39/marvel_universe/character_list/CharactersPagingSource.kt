package ar.com.p39.marvel_universe.character_list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ar.com.p39.marvel_universe.network.MarvelService
import retrofit2.HttpException
import javax.inject.Inject
import ar.com.p39.marvel_universe.network_models.Character
import java.io.IOException
import javax.inject.Singleton

@Singleton
class CharactersPagingSource @Inject constructor(
    private val service: MarvelService
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val pageIndex = params.key ?: 0
        return try {
            val response = service.getCharacters(20, pageIndex * 20)
            val characters: List<Character> = response.characterData.characters
            val nextKey = if (response.characterData.count == 0) {
                null
            } else {
                pageIndex + 1
            }
            LoadResult.Page(
                data = characters,
                prevKey = null,
                nextKey = nextKey,
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}