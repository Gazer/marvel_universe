package ar.com.p39.marvel_universe.character_details.ui

import ar.com.p39.marvel_universe.network_models.Character
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterSeriesFragment : CharacterBaseListFragment() {
    override fun getItem(character: Character): List<String> {
        return character.series.items.map { it.name }
    }
}