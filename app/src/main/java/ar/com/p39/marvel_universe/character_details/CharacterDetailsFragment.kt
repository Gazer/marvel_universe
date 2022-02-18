package ar.com.p39.marvel_universe.character_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.databinding.CharactersFragmentBinding
import ar.com.p39.marvel_universe.databinding.FragmentCharacterDetailsBinding

class CharacterDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCharacterDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCharacterDetailsBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }
}