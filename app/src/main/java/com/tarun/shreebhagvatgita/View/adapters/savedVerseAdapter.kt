package com.tarun.shreebhagvatgita.View.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarun.shreebhagvatgita.databinding.ViewVerseBinding
import com.tarun.shreebhagvatgita.DataSource.room.SavedVerse

class savedVerseAdapter(val onVerseClicked: (SavedVerse) -> Unit) : RecyclerView.Adapter<savedVerseAdapter.savedVerseViewHolder>() {

    val diffUtil = object : DiffUtil.ItemCallback<SavedVerse>() {
        override fun areItemsTheSame(oldItem: SavedVerse, newItem: SavedVerse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SavedVerse, newItem: SavedVerse): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class savedVerseViewHolder(var binding: ViewVerseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): savedVerseViewHolder {
        return savedVerseViewHolder(ViewVerseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: savedVerseViewHolder, position: Int) {
        val verse = differ.currentList[position]
        Log.d("VerseAdapter", "Binding verse at position: $position - $verse")
        holder.binding.apply {
            tvVerseNumber.text = "Verse ${verse.chapter_number}.${verse.verse_number}"
            tvVerseDetails.text = verse.translations[1]

            ll.setOnClickListener{
                onVerseClicked(verse)
            }
        }


    }
}

