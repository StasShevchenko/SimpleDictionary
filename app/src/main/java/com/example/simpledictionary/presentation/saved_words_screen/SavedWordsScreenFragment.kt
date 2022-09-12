package com.example.simpledictionary.presentation.saved_words_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpledictionary.R
import com.example.simpledictionary.databinding.SavedWordsScreenFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedWordsScreenFragment : Fragment(R.layout.saved_words_screen_fragment) {
    private val viewModel: SavedWordsScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = SavedWordsScreenFragmentBinding.bind(view)
        val savedWordsAdapter = SavedWordsAdapter({ wordInfo ->
            viewModel.deleteSavedWord(wordInfo)
        },
            { itemPosition ->
                viewModel.changeExpandedPosition(itemPosition)
            }
        )
        binding.apply {
            savedWordsRecyclerView.apply {
                itemAnimator = null
                adapter = savedWordsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedWords.collectLatest { savedWords ->
                    savedWordsAdapter.submitList(savedWords)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is SavedWordsScreenViewModel.UiEvent.WordIsDeleted -> {
                        Snackbar.make(binding.root, getString(R.string.word_deleted), Snackbar.LENGTH_LONG)
                            .setAnchorView(requireActivity().findViewById(R.id.bottom_navigation))
                            .setAction(getString(R.string.undo)){
                                viewModel.saveDeletedWord(event.wordInfo)
                            }
                            .show()
                    }
                }
            }
        }
    }
}