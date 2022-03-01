package ar.com.p39.marvel_universe.character_list.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.character_list.pagination.CharactersPagingSource
import ar.com.p39.marvel_universe.character_list.use_cases.GetAllCharacters
import ar.com.p39.marvel_universe.network_models.Character
import ar.com.p39.marvel_universe.network_models.CharacterDataContainer
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException


@ExperimentalCoroutinesApi
class CharactersPagingSourceTest : BaseTestCase() {
    private lateinit var charactersPagingSource: CharactersPagingSource

    @MockK
    lateinit var getAllCharacters: GetAllCharacters

    @Before
    fun setup() {
        charactersPagingSource = CharactersPagingSource(getAllCharacters)
    }

    @Test
    fun `getRefreshKey SHOULD always start from the start`() {
        // GIVEN
        val pagingState = PagingState(
            emptyList<PagingSource.LoadResult.Page<Int, Character>>(),
            0,
            PagingConfig(20),
            0,
        )

        // WHEN
        val result = charactersPagingSource.getRefreshKey(pagingState)

        // THEN
        assertEquals(0, result)
    }

    @Test
    fun `load SHOULD calculate next and prev page correctly for first page`() = runBlocking {
        // GIVEN
        val character = mockk<Character>()
        setCharacterResponse(character, 1, 3, 0)

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 1,
            placeholdersEnabled = false,
        )
        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(character),
            prevKey = null,
            nextKey = 1
        )

        // WHEN
        val result = charactersPagingSource.load(loadParams)

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `load SHOULD calculate next and prev page correctly for other pages`() = runBlocking {
        // GIVEN
        val character = mockk<Character>()
        setCharacterResponse(character, 1, 3, 1)

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 1,
            loadSize = 1,
            placeholdersEnabled = false,
        )
        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(character),
            prevKey = 0,
            nextKey = 2
        )

        // WHEN
        val result = charactersPagingSource.load(loadParams)

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `load SHOULD calculate next and prev page correctly for the last page`() = runBlocking {
        // GIVEN
        val character = mockk<Character>()
        setCharacterResponse(character, 1, 3, 2)

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 1,
            placeholdersEnabled = false,
        )
        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(character),
            prevKey = null,
            nextKey = null,
        )

        // WHEN
        val result = charactersPagingSource.load(loadParams)

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `load SHOULD return error on exception`() = runBlocking {
        // GIVEN
        val exceptionMessage = "io exception"
        val exception = IOException(exceptionMessage)
        coEvery { getAllCharacters(any(), any(), any()) } throws exception

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 1,
            placeholdersEnabled = false,
        )
        val expectedResult = PagingSource.LoadResult.Error<Int, Exception>(exception)

        // WHEN
        val result = charactersPagingSource.load(loadParams)

        // THEN
        assertEquals(expectedResult, result)
    }

    private fun setCharacterResponse(character: Character, count: Int, total: Int, offset: Int) {
        val characterDataContainer = mockk<CharacterDataContainer>()
        every { characterDataContainer.characters } returns listOf(character)
        every { characterDataContainer.count } returns count
        every { characterDataContainer.total } returns total
        every { characterDataContainer.offset } returns offset

        val characterDataWrapper = mockk<CharacterDataWrapper>()
        every { characterDataWrapper.characterData } returns characterDataContainer
        every { characterDataWrapper.code } returns "200"

        coEvery { getAllCharacters(any(), any(), any()) } returns characterDataWrapper
    }
}