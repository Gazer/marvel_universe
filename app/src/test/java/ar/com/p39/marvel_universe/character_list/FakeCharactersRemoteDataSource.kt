package ar.com.p39.marvel_universe.character_list

import androidx.paging.PagingData
import ar.com.p39.marvel_universe.network_models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCharactersRemoteDataSource : CharactersRemoteDataSource {
    private val names = listOf("Ant Man", "Hulk", "Other")

    override fun getCharacters(q: String?): Flow<PagingData<Character>> = flow {
        emit(
            PagingData.from(
                names.filter { if (q == null) true else it.startsWith(q) }.map {
                    Character(
                        ComicList(0, "", emptyList(), 0),
                        "",
                        EventList(0, "", emptyList(), 0),
                        "id-$it",
                        "",
                        it,
                        "",
                        SeriesList(0, "", emptyList(), 0),
                        StoryList(0, "", emptyList(), 0),
                        Thumbnail("", ""),
                        emptyList(),
                    )
                }
            )
        )
    }
}