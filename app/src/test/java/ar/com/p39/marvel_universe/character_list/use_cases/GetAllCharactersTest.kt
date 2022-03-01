package ar.com.p39.marvel_universe.character_list.use_cases

import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.character_list.repositories.MarvelCharactersRepository
import ar.com.p39.marvel_universe.network_models.Character
import ar.com.p39.marvel_universe.network_models.CharacterDataContainer
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllCharactersTest: BaseTestCase() {
    @MockK lateinit var marvelCharactersRepository: MarvelCharactersRepository

    lateinit var getAllCharacters: GetAllCharacters

    @Before
    fun setup() {
        getAllCharacters = GetAllCharacters(marvelCharactersRepository)
    }

    @Test
    fun `fetch all characters`() = runBlocking {
        // GIVE
        val character = mockk<Character>()
        setCharacterResponse(character, 1, 3, 0)

        // WHEN
        val result = getAllCharacters(null, 0, 0)

        // THEN
        assertEquals(character, result.characterData.characters.first())
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