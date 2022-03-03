package ar.com.p39.marvel_universe.character_details.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ar.com.p39.marvel_universe.character_details.CharacterDetailsStates
import ar.com.p39.marvel_universe.character_details.viewmodel.CharacterDetailsViewModel
import ar.com.p39.marvel_universe.network.MarvelService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class CharacterBaseFragment: Fragment() {
    @Inject
    lateinit var marvelService: MarvelService

    protected val viewModel: CharacterDetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MCU", viewModel.toString())
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            Log.d("MCU", "Summary::state")

            when (state) {
                is CharacterDetailsStates.Loaded -> handleLoaded(state)
                else -> {
                    // No-op
                }
            }
        }
    }

    protected abstract fun handleLoaded(state: CharacterDetailsStates.Loaded)
}