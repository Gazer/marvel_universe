package ar.com.p39.marvel_universe.character_details

import ar.com.p39.marvel_universe.network.MarvelService
import dagger.assisted.AssistedFactory

@AssistedFactory
interface CharacterDetailsViewModelFactory {
    fun create(marvelService: MarvelService): CharacterDetailsViewModel
}