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

    private val mutableCharacter: MutableLiveData<Character> = MutableLiveData()
    private val mutableLoadingError: MutableLiveData<String> = MutableLiveData()

    val character: LiveData<Character>
        get() = mutableCharacter
    val onError: LiveData<String>
        get() = mutableLoadingError

    fun fetchCharacter(characterId: String) {
        viewModelScope.launch {
            val response = marvelService.getCharacter(characterId)
            if (response.code == "200") {
                mutableCharacter.postValue(response.characterData.characters.first())
            } else {
                mutableLoadingError.postValue("Character not found: ${response.status}")
            }
        }
    }
}