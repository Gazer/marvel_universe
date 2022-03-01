package ar.com.p39.marvel_universe.character_details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.p39.marvel_universe.character_details.CharacterDetailsStates
import ar.com.p39.marvel_universe.character_details.use_cases.GetCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getCharacter: GetCharacter
) : ViewModel() {

    private val _uiState: MutableLiveData<CharacterDetailsStates> = MutableLiveData()

    val uiState: LiveData<CharacterDetailsStates>
        get() = _uiState

    fun fetchCharacter(characterId: String) {
        _uiState.value = CharacterDetailsStates.Loading
        viewModelScope.launch {
            try {
                val response = getCharacter(characterId)
                if (response.code == "200") {
                    _uiState.postValue(
                        CharacterDetailsStates.Loaded(response.characterData.characters.first())
                    )
                } else {
                    _uiState.postValue(
                        CharacterDetailsStates.Error("Character not found")
                    )
                }
            } catch (e: Exception) {
                _uiState.postValue(
                    CharacterDetailsStates.Error("No Internet Connection")
                )
            }
        }
    }
}