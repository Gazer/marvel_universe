package ar.com.p39.marvel_universe.character_list.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.character_list.models.CharactersResponse
import ar.com.p39.marvel_universe.character_list.pagination.CharactersPagingSource
import ar.com.p39.marvel_universe.character_list.use_cases.GetAllCharacters
import ar.com.p39.marvel_universe.common.Result
import ar.com.p39.marvel_universe.network_models.Character
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
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

    @MockK
    lateinit var character: Character

    @MockK
    lateinit var response: CharactersResponse

    @Before
    fun setup() {
        every { response.items } returns listOf(character)
        every { response.count } returns 1
        every { response.limit } returns 1
        every { response.offset } returns 1
        every { response.total } returns 3
        coEvery { getAllCharacters(any(), any(), any()) } returns Result.Success(response)

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
        every { response.offset } returns 2
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
}