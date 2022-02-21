package ar.com.p39.marvel_universe.character_list

import androidx.lifecycle.*
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
    private var _charactersFlow: MutableLiveData<Flow<PagingData<Character>>> = MutableLiveData()
    val charactersFlow: LiveData<Flow<PagingData<Character>>>
        get() = _charactersFlow

    var query: String? = null

    init {
        getCharacters()
    }

    fun setCharacterFilter(q: String?) {
        query = q
        viewModelScope.launch {
            try {
                val result = charactersRemoteDataSource.getCharacters(q).cachedIn(viewModelScope)
                _charactersFlow.postValue(result)
            } catch (ex: Exception) {
//                errorMessage.value = ex.message
            }
        }
    }

    private fun getCharacters() {
        viewModelScope.launch {
            try {
                val result = charactersRemoteDataSource.getCharacters(null).cachedIn(viewModelScope)
                _charactersFlow.postValue(result)
            } catch (ex: Exception) {
//                errorMessage.value = ex.message
            }
        }
    }
}