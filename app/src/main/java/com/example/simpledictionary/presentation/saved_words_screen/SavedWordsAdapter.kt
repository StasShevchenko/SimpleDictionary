package com.example.simpledictionary.presentation.saved_words_screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpledictionary.databinding.MeaningItemBinding
import com.example.simpledictionary.databinding.SavedWordHeaderBinding
import com.example.simpledictionary.databinding.SavedWordItemBinding
import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.presentation.saved_words_screen.SavedWordInfo.ExpandableWordInfo
import com.example.simpledictionary.presentation.saved_words_screen.SavedWordInfo.WordInfoHeader


class SavedWordsAdapter(
    private val onButtonClickAction: (WordInfo) -> Unit,
    private val onItemClickAction: (Int) -> Unit,
) : ListAdapter<SavedWordInfo, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val EXPANDABLE_WORD_VIEW = 0
        const val HEADER_VIEW = 1
    }

    inner class HeaderViewHolder(
        private val binding: SavedWordHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: WordInfoHeader) {
            binding.apply {
                headerTextView.text = header.letter
            }
        }
    }

    inner class ExpandableWordViewHolder(
        private val binding: SavedWordItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteImageButton.setOnClickListener {
                onButtonClickAction((getItem(bindingAdapterPosition) as ExpandableWordInfo).word)
            }
            binding.root.setOnClickListener {
                onItemClickAction(bindingAdapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(word: WordInfo, isExpanded: Boolean) {
            binding.apply {
                wordTextView.text = word.word
                if (isExpanded) {
                    if (word.phonetic == null) phoneticTextView.visibility = View.GONE
                    else
                        phoneticTextView.text = word.phonetic
                    //adding meanings items to word items
                    meaningsLinearLayout.removeAllViews()
                    word.meanings.forEach { meaning ->
                        val meaningItemBinding =
                            MeaningItemBinding.inflate(LayoutInflater.from(itemView.context))
                        meaningItemBinding.apply {
                            partOfSpeechTextView.text = meaning.partOfSpeech
                            //adding all the meanings to the meaning item
                            var definitionsCounter = 1
                            meaning.definitions.forEach { definition ->
                                val definitionTextView = TextView(itemView.context)
                                definitionTextView.text =
                                    definitionsCounter.toString() + ". " + definition.definition
                                definitionsCounter++
                                meaningItemBinding.definitionsLinearLayout.addView(
                                    definitionTextView
                                )
                                if (definition.example != null) {
                                    val exampleTextView = TextView(itemView.context)
                                    exampleTextView.text = "Example: " + definition.example
                                    meaningItemBinding.definitionsLinearLayout.addView(
                                        exampleTextView
                                    )
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
                                val antonymTextView = TextView(itemView.context)
                                var antonymText = ""
                                meaning.antonyms.forEach { antonym ->
                                    if (meaning.antonyms.indexOf(antonym) != meaning.antonyms.size - 1)
                                        antonymText += "$antonym, "
                                    else antonymText += antonym
                                }
                                antonymTextView.text = antonymText
                                meaningItemBinding.antonymsLinearLayout.addView(antonymTextView)
                            }
                        }
                        meaningsLinearLayout.addView(meaningItemBinding.root)
                    }
                    expandableLinearLayout.visibility = View.VISIBLE
                } else expandableLinearLayout.visibility = View.GONE
            }
        }
    }

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
        HEADER_VIEW -> {
            val binding = SavedWordHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            HeaderViewHolder(binding)
        }
        EXPANDABLE_WORD_VIEW -> {
            val binding = SavedWordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ExpandableWordViewHolder(binding)
        }
        else -> {
            val binding = SavedWordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ExpandableWordViewHolder(binding)
        }
    }
}

override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder.itemViewType) {
        EXPANDABLE_WORD_VIEW -> {
            val currentWord = (getItem(position) as ExpandableWordInfo)
            (holder as ExpandableWordViewHolder).bind(currentWord.word, currentWord.isExpanded)
        }
        HEADER_VIEW -> {
            val currentHeader = (getItem(position) as WordInfoHeader)
            (holder as HeaderViewHolder).bind(currentHeader)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<SavedWordInfo>() {
    override fun areItemsTheSame(
        oldItem: SavedWordInfo,
        newItem: SavedWordInfo
    ): Boolean {
        if (oldItem is ExpandableWordInfo
            && newItem is ExpandableWordInfo
            && (oldItem.word.meanings == newItem.word.meanings)
        ) {
            return true
        } else if (oldItem is WordInfoHeader && newItem is WordInfoHeader && (oldItem.letter == newItem.letter)) {
            return true
        }
        return false
    }

    override fun areContentsTheSame(
        oldItem: SavedWordInfo,
        newItem: SavedWordInfo
    ): Boolean {
        if (oldItem is ExpandableWordInfo
            && newItem is ExpandableWordInfo
            && (oldItem == newItem)
        ) {
            return true
        } else if (oldItem is WordInfoHeader && newItem is WordInfoHeader && (oldItem.letter == newItem.letter)) {
            return true
        }
        return false
    }
}

override fun getItemViewType(position: Int): Int {
    return when (getItem(position)) {
        is WordInfoHeader -> {
            HEADER_VIEW
        }
        is ExpandableWordInfo -> {
            EXPANDABLE_WORD_VIEW
        }
    }
}
}