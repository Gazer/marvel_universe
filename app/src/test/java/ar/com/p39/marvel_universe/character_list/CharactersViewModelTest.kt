package ar.com.p39.marvel_universe.character_list

import androidx.lifecycle.SavedStateHandle
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import ar.com.p39.marvel_universe.BaseTestCase
import ar.com.p39.marvel_universe.character_list.adapters.CharactersAdapter
import ar.com.p39.marvel_universe.network_models.Character
import ar.com.p39.marvel_universe.utils.observeOnce
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharactersViewModelTest : BaseTestCase() {
    private lateinit var viewModel: CharactersViewModel

    @MockK
    private lateinit var charactersRemoteDataSource: CharactersRemoteDataSource

    @MockK
    private lateinit var character: Character

    @Before
    fun setup() {
        viewModel = CharactersViewModel(SavedStateHandle(), charactersRemoteDataSource)
    }

    @Test
    fun `SHOULD load characters on creation`() = runTest {
        // GIVEN
        every { charactersRemoteDataSource.getCharacters(any()) } returns flow {
            emit(PagingData.from(listOf(character)))
        }
        val differ = AsyncPagingDataDiffer(
            diffCallback = CharactersAdapter.CharacterComparator,
            updateCallback = noopListUpdateCallback,
        )
        var job: Job? = null

        // WHEN
        viewModel.charactersFlow.observeOnce {
            job = launch {
                it.collectLatest { pagingData ->
                    differ.submitData(pagingData)
                }
            }
        }
        advanceUntilIdle()

        // THEN
        assertThat(differ.snapshot().items.size, `is`(1))
        assertThat(differ.snapshot().items.first(), `is`(character))
        job?.cancel()
    }

    @Test
    fun `SHOULD set filter to search characters`() = runTest {
        // GIVEN
        every { character.name } returns "Hulk"
        every { charactersRemoteDataSource.getCharacters(any()) } returns flow {
            emit(PagingData.from(listOf(character)))
        }
        val differ = AsyncPagingDataDiffer(
            diffCallback = CharactersAdapter.CharacterComparator,
            updateCallback = noopListUpdateCallback,
        )
        var job: Job? = null

        // WHEN
        viewModel.setCharacterFilter("H")
        advanceUntilIdle()

        viewModel.charactersFlow.observeOnce {
            job = launch {
                it.collectLatest { pagingData ->
                    differ.submitData(pagingData)
                }
            }
        }
        advanceUntilIdle()

        // THEN
        assertThat(differ.snapshot().items.size, `is`(1))
        assertThat(differ.snapshot().items.first().name, `is`("Hulk"))
        assertThat(viewModel.query, `is`("H"))
        job?.cancel()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}