package ar.com.p39.marvel_universe.character_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.character_details.CharacterDetailsStates
import ar.com.p39.marvel_universe.databinding.FragmentCharacterSummaryBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharacterSummaryFragment : CharacterBaseFragment() {
    private lateinit var binding: FragmentCharacterSummaryBinding

    @Inject
    lateinit var picasso: Picasso

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCharacterSummaryBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun handleLoaded(state: CharacterDetailsStates.Loaded) {
        val character = state.character
        val url = "${character.thumbnail.path}/landscape_amazing.${character.thumbnail.extension}"

        with(binding) {
            name.text = character.name
            description.text = character.description
        }
        if (url.contains("image_not_available")) {
            picasso.load(R.mipmap.placeholder)
                .into(binding.image)
        } else {
            picasso
                .load(url)
                .placeholder(R.mipmap.placeholder)
                .into(binding.image)
        }
    }
}