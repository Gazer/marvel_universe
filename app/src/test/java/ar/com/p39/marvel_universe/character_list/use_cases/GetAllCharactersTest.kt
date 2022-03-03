package ar.com.p39.marvel_universe.character_list.use_cases

import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.character_list.models.CharactersResponse
import ar.com.p39.marvel_universe.character_list.repositories.MarvelCharactersRepository
import ar.com.p39.marvel_universe.common.Result
import ar.com.p39.marvel_universe.network_models.Character
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllCharactersTest: BaseTestCase() {
    @MockK lateinit var repository: MarvelCharactersRepository

    lateinit var getAllCharacters: GetAllCharacters

    @Before
    fun setup() {
        getAllCharacters = GetAllCharacters(repository)
    }

    @Test
    fun `fetch all characters`() = runBlocking {
        // GIVE
        val character = mockk<Character>()
        val response = mockk<CharactersResponse>()
        every { response.items } returns listOf(character)
        coEvery { repository.getCharacters(any(), any(), any()) } returns Result.Success(response)

        // WHEN
        val result = getAllCharacters(null, 0, 0)

        // THEN
        assert(Result.Success::class.java == result.javaClass)
        assertEquals(character, (result as Result.Success).data.items.first())
    }
}