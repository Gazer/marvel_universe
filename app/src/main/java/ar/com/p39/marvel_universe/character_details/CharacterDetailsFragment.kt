package ar.com.p39.marvel_universe.character_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.character_details.CharacterDetailsModule.provideFactory
import ar.com.p39.marvel_universe.databinding.FragmentCharacterDetailsBinding
import ar.com.p39.marvel_universe.network.MarvelService
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCharacterDetailsBinding

    @Inject
    lateinit var marvelService: MarvelService

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var factory: CharacterDetailsViewModelFactory
    private val viewModel: CharacterDetailsViewModel by viewModels {
        provideFactory(
            factory,
            marvelService,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCharacterDetailsBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.character.observe(viewLifecycleOwner) { character ->
            Log.d("MCU", "Got character: ${character.description}")
            val url = "${character.thumbnail.path}.${character.thumbnail.extension}"

            with(binding) {
                name.text = character.name
                description.text = character.description
            }
            if (url.contains("image_not_available")) {
                picasso.load(R.mipmap.placeholder)
                    .fit()
                    .into(binding.image)
            } else {
                picasso
                    .load(url)
                    .fit()
                    .placeholder(R.mipmap.placeholder)
                    .into(binding.image)
            }
        }

        viewModel.onError.observe(viewLifecycleOwner) {
            Log.d("MCU", "Got error: $it")
        }

        lifecycleScope.launchWhenResumed {
            viewModel.fetchCharacter(arguments?.getString("characterId", "") ?: "")
        }

    }
}