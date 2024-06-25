package com.tarun.shreebhagvatgita.View.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarun.shreebhagvatgita.R
import com.tarun.shreebhagvatgita.databinding.ViewVerseBinding
import kotlin.reflect.KFunction1

class VerseAdapter(
    private val fragment: Fragment,
    private val onVerseClicked: (Int) -> Unit,
    private val b: Boolean
) : RecyclerView.Adapter<VerseAdapter.VerseViewHolder>() {

    val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class VerseViewHolder(var binding: ViewVerseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        return VerseViewHolder(ViewVerseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        val verse = differ.currentList[position]
        Log.d("VerseAdapter", "Binding verse at position: $position - $verse")
        holder.binding.apply {
            tvVerseNumber.text = "Verse ${position + 1}"
            tvVerseDetails.text = verse

            ll.setOnClickListener {
                onVerseClicked(position + 1)
            }
        }


        }
    }

