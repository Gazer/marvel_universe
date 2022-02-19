package ar.com.p39.marvel_universe.character_list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ar.com.p39.marvel_universe.network_models.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val charactersRemoteDataSource: CharactersRemoteDataSource
) : ViewModel() {
    private lateinit var _charactersFlow: Flow<PagingData<Character>>
    val charactersFlow: Flow<PagingData<Character>>
        get() = _charactersFlow

    init {
        getCharacters()
    }

    private fun getCharacters() {
        viewModelScope.launch {
            try {
                val result = charactersRemoteDataSource.getCharacters().cachedIn(viewModelScope)
                _charactersFlow = result
            } catch (ex: Exception) {
//                errorMessage.value = ex.message
            }
        }
    }
}