package ar.com.p39.marvel_universe.character_details.ui

import ar.com.p39.marvel_universe.network_models.Character

class CharacterEventsFragment : CharacterBaseListFragment() {
    override fun getItem(character: Character): List<String> {
        return character.events.items.map { it.name }
    }
}