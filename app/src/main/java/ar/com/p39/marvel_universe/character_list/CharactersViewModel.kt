package ar.com.p39.marvel_universe.character_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ar.com.p39.marvel_universe.network_models.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val charactersRemoteDataSource: CharactersRemoteDataSource
) : ViewModel() {

    fun getCharacters(): Flow<PagingData<Character>> {
        return charactersRemoteDataSource.getCharacters().cachedIn(viewModelScope)
    }
}