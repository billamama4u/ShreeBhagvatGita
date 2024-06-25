package com.tarun.shreebhagvatgita.View.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tarun.shreebhagvatgita.R
import com.tarun.shreebhagvatgita.View.adapters.savedVerseAdapter
import com.tarun.shreebhagvatgita.ViewModel.MainViewModel
import com.tarun.shreebhagvatgita.databinding.FragmentSavedVerseBinding
import com.tarun.shreebhagvatgita.DataSource.room.SavedVerse
import kotlinx.coroutines.launch

class SavedVerseFragment : Fragment() {

    private lateinit var binding: FragmentSavedVerseBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: savedVerseAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedVerseBinding.inflate(inflater, container, false)
        setupRecyclerView()
        getandset()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = savedVerseAdapter(::onVerseClicked)
        binding.rvVerse.adapter = adapter
        binding.rvVerse.layoutManager = LinearLayoutManager(context)
        Log.d("VerseFragment", "RecyclerView setup complete")
    }

    private fun getandset() {
        lifecycleScope.launch {
            viewModel.getAllSavedVerses()!!.observe(viewLifecycleOwner) { savedVerses ->

                adapter.differ.submitList(savedVerses)
                binding.shimmer.visibility = View.GONE
                binding.rvVerse.visibility = View.VISIBLE
            }
        }
    }

    private fun onVerseClicked(savedVerse: SavedVerse) {
        val bundle = Bundle().apply {
            putInt("chapterNumber",savedVerse.chapter_number)
            putInt("verseNumber",savedVerse.verse_number)
            putBoolean("callApi",false)
        }
        findNavController().navigate(R.id.action_savedVerseFragment_to_verseDetailFragment,bundle)

    }
}
