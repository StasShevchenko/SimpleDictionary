package com.example.simpledictionary.presentation.dictionary_screen


import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpledictionary.databinding.MeaningItemBinding
import com.example.simpledictionary.databinding.WordItemBinding
import com.example.simpledictionary.domain.model.WordInfo

class WordsAdapter() : ListAdapter<WordInfo, WordsAdapter.WordViewHolder>(DiffCallback()) {

    inner class WordViewHolder(
        private val binding: WordItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(word: WordInfo) {
            binding.apply {
                wordTextView.text = word.word
                if(word.phonetic == null) phoneticTextView.visibility = View.GONE
                else
                phoneticTextView.text = word.phonetic
                //adding meanings items to word items
                word.meanings.forEach { meaning ->
                    val meaningItemBinding =
                        MeaningItemBinding.inflate(LayoutInflater.from(itemView.context))
                    meaningsLinearLayout.removeAllViews()
                    meaningItemBinding.apply {
                        partOfSpeechTextView.text = meaning.partOfSpeech
                        //adding all the meanings to the meaning item
                        var definitionsCounter = 1
                        meaning.definitions.forEach { definition ->
                            val definitionTextView = TextView(itemView.context)
                            definitionTextView.text =
                                definitionsCounter.toString() + ". " + definition.definition
                            definitionsCounter++
                            meaningItemBinding.definitionsLinearLayout.addView(definitionTextView)
                            if (definition.example != null) {
                                val exampleTextView = TextView(itemView.context)
                                exampleTextView.text = "Example: " + definition.example
                                meaningItemBinding.definitionsLinearLayout.addView(exampleTextView)
                            }
                        }
                        //adding all the synonyms to the meaning item
                        if (meaning.synonyms.isNotEmpty()) {
                            meaningItemBinding.synonymsHeaderTextView.visibility = View.VISIBLE
                            val synonymTextView = TextView(itemView.context)
                            var synonymText = ""
                            meaning.synonyms.forEach { synonym ->
                                if (meaning.synonyms.indexOf(synonym) != meaning.synonyms.size - 1)
                                    synonymText += "$synonym, "
                                else synonymText += synonym
                            }
                            synonymTextView.text = synonymText
                            meaningItemBinding.synonymsLinearLayout.addView(synonymTextView)
                        }

                        if (meaning.antonyms.isNotEmpty()) {
                            meaningItemBinding.antonymsHeaderTextView.visibility = View.VISIBLE
                            val synonymTextView = TextView(itemView.context)
                            var antonymText = ""
                            meaning.antonyms.forEach { antonym ->
                                if (meaning.antonyms.indexOf(antonym) != meaning.antonyms.size - 1)
                                    antonymText += "$antonym, "
                                else antonymText += antonym
                            }
                            meaningItemBinding.synonymsLinearLayout.addView(synonymTextView)
                        }
                    }
                    meaningsLinearLayout.addView(meaningItemBinding.root)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val currentWord = getItem(position)
        holder.bind(currentWord)
    }

    class DiffCallback : DiffUtil.ItemCallback<WordInfo>() {
        override fun areItemsTheSame(oldItem: WordInfo, newItem: WordInfo): Boolean {
            return oldItem.meanings == newItem.meanings
        }

        override fun areContentsTheSame(oldItem: WordInfo, newItem: WordInfo): Boolean {
            return oldItem == newItem
        }
    }
}