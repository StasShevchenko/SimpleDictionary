package com.example.simpledictionary.presentation.dictionary_screen

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpledictionary.R
import com.example.simpledictionary.databinding.DictionaryScreenFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DictionaryScreenFragment : Fragment(R.layout.dictionary_screen_fragment) {
    private val viewModel: DictionaryScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DictionaryScreenFragmentBinding.bind(view)

        val wordsAdapter = WordsAdapter()
        binding.apply {
            searchWordEditText.addTextChangedListener {
                viewModel.searchWord(it.toString())
            }

            wordsRv.apply {
                adapter = wordsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wordsState.collectLatest { wordsState ->
                    if (wordsState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.INVISIBLE
                    } else if(wordsState.isError){
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorTextView.text = wordsState.errorMessage
                        binding.errorTextView.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorTextView.visibility = View.INVISIBLE
                    }
                    wordsAdapter.submitList(wordsState.words)
                }
            }
        }
    }
}