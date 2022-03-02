package ar.com.p39.marvel_universe.character_details.use_cases

import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.character_details.repositories.MarvelCharacterRepository
import ar.com.p39.marvel_universe.common.Result
import ar.com.p39.marvel_universe.network_models.Character
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCharacterTest: BaseTestCase() {
    @MockK
    lateinit var repository: MarvelCharacterRepository

    lateinit var getCharacter: GetCharacter

    @Before
    fun setup() {
        getCharacter = GetCharacter(repository)
    }

    @Test
    fun `fetch all characters`() = runBlocking {
        // GIVE
        val character = mockk<Character>()
        coEvery { repository.getCharacter(any()) } returns Result.Success(character)

        // WHEN
        val result = getCharacter("someId")

        // THEN
        assert(Result.Success::class.java == result.javaClass)
        TestCase.assertEquals(character, (result as Result.Success).data)
    }
}