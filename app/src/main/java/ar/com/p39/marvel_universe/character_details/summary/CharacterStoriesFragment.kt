package ar.com.p39.marvel_universe.character_details.summary

import ar.com.p39.marvel_universe.network_models.Character

class CharacterStoriesFragment: CharacterBaseListFragment() {
    override fun getItem(character: Character): List<String> {
        return character.stories.items.map { it.name }
    }
}