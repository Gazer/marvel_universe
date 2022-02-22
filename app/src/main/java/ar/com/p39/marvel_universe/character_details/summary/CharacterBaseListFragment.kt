package ar.com.p39.marvel_universe.character_details.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.character_details.CharacterDetailsStates
import ar.com.p39.marvel_universe.character_details.summary.adapters.ListAdapter
import ar.com.p39.marvel_universe.databinding.FragmentCharacterBaseListBinding
import ar.com.p39.marvel_universe.network_models.Character

abstract class CharacterBaseListFragment: CharacterBaseFragment() {
    private lateinit var binding: FragmentCharacterBaseListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCharacterBaseListBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CharacterDetailsStates.Loaded -> handleLoaded(state)
                else -> {
                    // No-op
                }
            }
        }
    }

    override fun handleLoaded(state: CharacterDetailsStates.Loaded) {
        val character = state.character

        with(binding) {
            val items = getItem(character)

            if (items.isEmpty()) {
                list.isVisible = false
                image.setImageResource(R.drawable.ic_undraw_not)
                image.isVisible = true
                notFound.isVisible = true
            } else {
                list.adapter = ListAdapter(items) {
                    // TODO: Navigate
                }
                list.layoutManager = LinearLayoutManager(requireContext())

                list.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    )
                )
            }
        }
    }

    protected abstract fun getItem(character: Character): List<String>
}