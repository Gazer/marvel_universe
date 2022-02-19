package ar.com.p39.marvel_universe.character_details

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.p39.marvel_universe.network.MarvelService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

class CharacterDetailsViewModel @AssistedInject constructor(
    @Assisted private val marvelService: MarvelService,
) : ViewModel() {

    private val _uiState: MutableLiveData<CharacterDetailsStates> = MutableLiveData()

    val uiState: LiveData<CharacterDetailsStates>
        get() = _uiState

    fun fetchCharacter(characterId: String) {
        _uiState.value = CharacterDetailsStates.Loading
        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {
                _uiState.postValue(
                    CharacterDetailsStates.Error("No Internet Connection")
                )
            }
        }
    }
}