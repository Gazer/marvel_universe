package ar.com.p39.marvel_universe.character_details

import androidx.lifecycle.*
import ar.com.p39.marvel_universe.network.MarvelService
import ar.com.p39.marvel_universe.network_models.Character
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CharacterDetailsViewModel @AssistedInject constructor(
    @Assisted private val marvelService: MarvelService,
) : ViewModel() {

    private val _uiState: MutableLiveData<CharacterDetailsStates> = MutableLiveData()

    val uiState: LiveData<CharacterDetailsStates>
        get() = _uiState

    fun fetchCharacter(characterId: String) {
        _uiState.value = CharacterDetailsStates.Loading
        viewModelScope.launch {
            val response = marvelService.getCharacter(characterId)
            if (response.code == "200") {
                _uiState.postValue(
                    CharacterDetailsStates.Loaded(response.characterData.characters.first())
                )
            } else {
                _uiState.postValue(
                    CharacterDetailsStates.Error("Character not found: ${response.status}")
                )
            }
        }
    }
}