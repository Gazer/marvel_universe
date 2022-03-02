package ar.com.p39.marvel_universe.character_details.repositories

import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.network.MarvelService
import ar.com.p39.marvel_universe.network_models.Character
import ar.com.p39.marvel_universe.common.Result
import ar.com.p39.marvel_universe.network_models.CharacterDataContainer
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MarvelCharacterRepositoryTest: BaseTestCase() {
    @MockK lateinit var marvelService: MarvelService

    @MockK lateinit var character: Character

    @MockK lateinit var characterDataWrapper: CharacterDataWrapper

    @MockK lateinit var characterDataContainer: CharacterDataContainer

    lateinit var marvelCharacterRepository: MarvelCharacterRepository

    @Before
    fun setup() {
        every { characterDataContainer.total } returns 1
        every { characterDataContainer.offset } returns 0
        every { characterDataContainer.characters } returns listOf(character)
        every { characterDataWrapper.characterData } returns characterDataContainer
        every { characterDataWrapper.code } returns "200"
        coEvery { marvelService.getCharacter(any()) } returns characterDataWrapper

        marvelCharacterRepository = MarvelCharacterRepository(marvelService)
    }

    @Test
    fun `getCharacter SHOULD detect if the character id is invalid`() = runBlocking {
        // GIVEN
        every { characterDataWrapper.code } returns "404"
        every { characterDataWrapper.status } returns "NOT FOUND"

        // WHEN
        val result = marvelCharacterRepository.getCharacter("123")

        // THEN
        assertEquals(Result.Error::class.java, result.javaClass)
        assertEquals("NOT FOUND", (result as Result.Error).error)
    }

    @Test
    fun `getCharacter SHOULD fetch the character`() = runBlocking {
        // GIVEN

        // WHEN
        val result = marvelCharacterRepository.getCharacter("123")

        // THEN
        assertEquals(Result.Success::class.java, result.javaClass)
        assertEquals(character, (result as Result.Success).data)
    }

    @Test
    fun `getCharacter SHOULD return error if the API call fails`() = runBlocking {
        // GIVEN
        coEvery { marvelService.getCharacter(any()) } throws Exception("Error")

        // WHEN
        val result = marvelCharacterRepository.getCharacter("123")

        // THEN
        assertEquals(Result.Error::class.java, result.javaClass)
        assertEquals("Error", (result as Result.Error).error)
    }
}