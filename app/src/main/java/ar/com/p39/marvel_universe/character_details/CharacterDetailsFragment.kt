package ar.com.p39.marvel_universe.character_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.character_details.CharacterDetailsModule.provideFactory
import ar.com.p39.marvel_universe.databinding.FragmentCharacterDetailsBinding
import ar.com.p39.marvel_universe.network.MarvelService
import com.google.android.material.snackbar.Snackbar
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
    private val viewModel: CharacterDetailsViewModel by activityViewModels {
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

        Log.d("MCU", viewModel.toString())
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            Log.d("MCU", "Details::state")
            when (state) {
                is CharacterDetailsStates.Error -> handleError(state)
                is CharacterDetailsStates.Loaded -> handleLoaded(state)
                CharacterDetailsStates.Loading -> handleLoading(state)
            }
        }

        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = nestedNavHostFragment.navController
        setupWithNavController(
            binding.navigation,
            navController
        )
        fetchData()
    }

    private fun fetchData() {
        lifecycleScope.launchWhenResumed {
            viewModel.fetchCharacter(arguments?.getString("characterId", "") ?: "")
        }
    }

    private fun handleLoading(state: CharacterDetailsStates) {
        binding.loading.visibility = View.VISIBLE
    }

    private fun handleLoaded(state: CharacterDetailsStates.Loaded) {
        binding.loading.visibility = View.GONE
    }

    private fun handleError(state: CharacterDetailsStates.Error) {
        binding.loading.visibility = View.GONE

        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_INDEFINITE).apply {
            setAction("Retry") {
                fetchData()
            }
        }.show()
    }
}