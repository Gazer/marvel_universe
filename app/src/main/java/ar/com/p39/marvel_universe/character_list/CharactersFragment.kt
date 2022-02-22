package ar.com.p39.marvel_universe.character_list

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.character_list.adapters.CharactersAdapter
import ar.com.p39.marvel_universe.character_list.adapters.CharactersLoadingStateAdapter
import ar.com.p39.marvel_universe.databinding.FragmentCharactersBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private val viewModel: CharactersViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private lateinit var binding: FragmentCharactersBinding
    private lateinit var adapter: CharactersAdapter
    private lateinit var navHostFragment: NavHostFragment
    private var isLargeDevice: Boolean = false

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
        isLargeDevice = resources.getBoolean(R.bool.isTablet)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCharactersBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLargeDevice) {
            setupNavHost()
        }
        initViews()
        initList()
        collectData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.characters_menu, menu)

        val search: MenuItem = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                viewModel.setCharacterFilter(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        search.setOnActionExpandListener(
            object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    // No-op
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    viewModel.setCharacterFilter(null)
                    return true
                }
            }
        )
        viewModel.query?.let {
            search.expandActionView()
            searchView.setQuery(it, false)
        }
    }

    private fun collectData() {
        viewModel.charactersFlow.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                it.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.noResults.isVisible = adapter.itemCount == 0 && loadStates.refresh is LoadState.NotLoading
                binding.loading.isVisible = loadStates.refresh is LoadState.Loading
                binding.noInternet.isVisible = loadStates.refresh is LoadState.Error
            }
        }
    }

    private fun initViews() {
        binding.retry.setOnClickListener {
            adapter.retry()
        }
        // TODO: Needed for Shared element transaction
        binding.list.apply {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (viewModel.query.isNullOrBlank()) {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } else {
                viewModel.query = null
                viewModel.setCharacterFilter(null)
            }
        }
        callback.isEnabled = true
    }

    private fun initList() {
        adapter = CharactersAdapter(picasso, isLargeDevice) { character ->
            val bundle = Bundle().apply {
                putString("characterId", character.id)
                putString("characterName", character.name)
            }
            navHostFragment.findNavController().navigate(R.id.characterDetailsFragment, bundle)
        }.apply {
            stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = CharactersLoadingStateAdapter(adapter),
            footer = CharactersLoadingStateAdapter(adapter),
        )
    }

    // Set up the NavHost Fragment for the Tablet Layout
    private fun setupNavHost() {
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.detail_container) as NavHostFragment
    }
}