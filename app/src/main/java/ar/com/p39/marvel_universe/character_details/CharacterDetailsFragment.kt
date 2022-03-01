package ar.com.p39.marvel_universe.character_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.character_details.viewmodel.CharacterDetailsViewModel
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

    private val viewModel: CharacterDetailsViewModel by activityViewModels()

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

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CharacterDetailsStates.Error -> handleError(state)
                is CharacterDetailsStates.Loaded -> handleLoaded()
                CharacterDetailsStates.Loading -> handleLoading()
            }
        }

        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = nestedNavHostFragment.navController
        setupWithNavController(
            binding.navigation,
            navController
        )
        fetchData()
    }

    private fun fetchData() {
        if (arguments?.containsKey("characterId") == true) {
            lifecycleScope.launchWhenResumed {
                viewModel.fetchCharacter(arguments?.getString("characterId", "") ?: "")
            }
        } else {
            binding.loading.visibility = View.GONE
            binding.navigation.visibility = View.GONE
        }
    }

    private fun handleLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun handleLoaded() {
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