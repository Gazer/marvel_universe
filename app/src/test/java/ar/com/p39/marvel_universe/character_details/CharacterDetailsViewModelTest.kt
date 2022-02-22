package ar.com.p39.marvel_universe.character_details

import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.network.MarvelService
import ar.com.p39.marvel_universe.network_models.Character
import ar.com.p39.marvel_universe.network_models.CharacterDataContainer
import ar.com.p39.marvel_universe.network_models.CharacterDataWrapper
import ar.com.p39.marvel_universe.utils.skipFirstAndObserveOnce
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CharacterDetailsViewModelTest : BaseTestCase() {
    private lateinit var viewModel: CharacterDetailsViewModel

    @MockK
    lateinit var marvelService: MarvelService

    @MockK
    lateinit var character: Character

    @Before
    fun setup() {
        viewModel = CharacterDetailsViewModel(marvelService)
    }

    @Test
    fun `fetchCharacter SHOULD load a character`() = runTest {
        // GIVEN
        setCharacterResponse(character, 1, 1, 0)

        // WHEN
        lateinit var uiState: CharacterDetailsStates
        viewModel.uiState.skipFirstAndObserveOnce {
            uiState = it
        }
        viewModel.fetchCharacter("someId")
        advanceUntilIdle()

        // THEN
        assert(uiState is CharacterDetailsStates.Loaded)
        assertEquals(character, (uiState as CharacterDetailsStates.Loaded).character)
    }

    @Test
    fun `fetchCharacter SHOULD detect if there is no connection`() = runTest {
        // GIVEN
        coEvery { marvelService.getCharacter(any()) } throws IOException()

        // WHEN
        lateinit var uiState: CharacterDetailsStates
        viewModel.uiState.skipFirstAndObserveOnce {
            uiState = it
        }
        viewModel.fetchCharacter("someId")
        advanceUntilIdle()

        // THEN
        assert(uiState is CharacterDetailsStates.Error)
        assertEquals("No Internet Connection", (uiState as CharacterDetailsStates.Error).error)
    }

    @Test
    fun `fetchCharacter SHOULD detect if the character id is invalid`() = runTest {
        // GIVEN
        setCharacterResponse(character, 1, 1, 0, "404")

        // WHEN
        lateinit var uiState: CharacterDetailsStates
        viewModel.uiState.skipFirstAndObserveOnce {
            uiState = it
        }
        viewModel.fetchCharacter("someId")
        advanceUntilIdle()

        // THEN
        assert(uiState is CharacterDetailsStates.Error)
        assertEquals("Character not found", (uiState as CharacterDetailsStates.Error).error)
    }

    private fun setCharacterResponse(character: Character, count: Int, total: Int, offset: Int, code: String = "200") {
        val characterDataContainer = mockk<CharacterDataContainer>()
        every { characterDataContainer.characters } returns listOf(character)
        every { characterDataContainer.count } returns count
        every { characterDataContainer.total } returns total
        every { characterDataContainer.offset } returns offset

        val characterDataWrapper = mockk<CharacterDataWrapper>()
        every { characterDataWrapper.characterData } returns characterDataContainer
        every { characterDataWrapper.code } returns code

        coEvery { marvelService.getCharacter(any()) } returns characterDataWrapper
    }
}