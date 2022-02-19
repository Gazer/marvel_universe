package ar.com.p39.marvel_universe.character_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.p39.marvel_universe.R
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

    @Inject
    lateinit var picasso: Picasso

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
        initView()
        fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.list.adapter = null
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.charactersFlow.collectLatest {
                adapter.submitData(it)
            }
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.noInternet.isVisible = loadState.refresh is LoadState.Error
            }
        }
    }

    private fun initView() {
        if (!::adapter.isInitialized) {
            adapter = CharactersAdapter(picasso).apply {
                stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
        binding.retry.setOnClickListener {
            adapter.retry()
        }
        binding.list.apply {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = CharactersLoadingStateAdapter(adapter),
            footer = CharactersLoadingStateAdapter(adapter),
        )
    }
}