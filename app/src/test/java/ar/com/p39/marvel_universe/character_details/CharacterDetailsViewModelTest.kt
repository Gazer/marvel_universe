package ar.com.p39.marvel_universe.character_details

import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.character_details.use_cases.GetCharacter
import ar.com.p39.marvel_universe.character_details.viewmodel.CharacterDetailsViewModel
import ar.com.p39.marvel_universe.common.Result
import ar.com.p39.marvel_universe.network_models.Character
import ar.com.p39.marvel_universe.utils.skipFirstAndObserveOnce
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
    lateinit var getCharacter: GetCharacter

    @MockK
    lateinit var character: Character

    @Before
    fun setup() {
        coEvery { getCharacter(any()) } returns Result.Success(character)

        viewModel = CharacterDetailsViewModel(getCharacter)
    }

    @Test
    fun `fetchCharacter SHOULD load a character`() = runTest {
        // GIVEN

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
        coEvery { getCharacter(any()) } throws IOException()

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
    fun `fetchCharacter SHOULD detect there was an error fetching the character`() = runTest {
        // GIVEN
        coEvery { getCharacter(any()) } returns Result.Error("Some error")

        // WHEN
        lateinit var uiState: CharacterDetailsStates
        viewModel.uiState.skipFirstAndObserveOnce {
            uiState = it
        }
        viewModel.fetchCharacter("someId")
        advanceUntilIdle()

        // THEN
        assert(uiState is CharacterDetailsStates.Error)
        assertEquals("Some error", (uiState as CharacterDetailsStates.Error).error)
    }
}