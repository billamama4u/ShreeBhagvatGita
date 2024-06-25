package com.tarun.shreebhagvatgita.View.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tarun.shreebhagvatgita.NetworManager
import com.tarun.shreebhagvatgita.R
import com.tarun.shreebhagvatgita.View.adapters.VerseAdapter
import com.tarun.shreebhagvatgita.ViewModel.MainViewModel
import com.tarun.shreebhagvatgita.databinding.FragmentVerseBinding
import kotlinx.coroutines.launch

class VerseFragment : Fragment() {
    private lateinit var binding: FragmentVerseBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: VerseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerseBinding.inflate(inflater, container, false)

        setupRecyclerView()
        if(arguments?.getBoolean("callApi") == true) {
            getAndSetArguments()
        }
        checkInternet()
        onReadMoreClicked()

        return binding.root
    }


    private fun setupRecyclerView() {
        adapter = VerseAdapter(this,::onVerseClicked, true)
        binding.rvVerse.adapter = adapter
        binding.rvVerse.layoutManager = LinearLayoutManager(context)
        Log.d("VerseFragment", "RecyclerView setup complete")
    }

    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.tvseemore.setOnClickListener {
            isExpanded = if (!isExpanded) {
                binding.tvChapterDescription.maxLines = 20
                true
            } else {
                binding.tvChapterDescription.maxLines = 4
                false
            }
        }
    }

    private fun getAndSetArguments() {
        val bundle = arguments
        binding.tvChapterName.text = bundle?.getString("chapterName")
        binding.tvChapterDescription.text = bundle?.getString("chapterSummary")
        binding.tvChapterNumber.text = "Chapter ${bundle?.getInt("chapterNumber", 0)}"
        binding.tvVerseCount.text = "Verses: ${bundle?.getInt("versesCount", 0)}"

        val chapterNumber = bundle?.getInt("chapterNumber", 0) ?: 0
        Log.d("VerseFragment", "Fetching verses for chapter $chapterNumber")
        viewModel.fetchVerses(chapterNumber)
    }

    private fun checkInternet() {
        val networkManager = NetworManager(requireContext())
        networkManager.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.shimmer.visibility = View.VISIBLE
                binding.rvVerse.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.GONE
                getAllVerses()

            } else {
                binding.shimmer.visibility = View.GONE
                binding.rvVerse.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
            }
        })
    }

    private fun getAllVerses() {
        if (arguments?.getBoolean("callApi") == true) {
            Log.d("VerseFragment", "Setting up observer for verses")
            viewModel.verses.observe(viewLifecycleOwner, Observer { verses ->
                Log.d("VerseFragment", "Observed verses: ${verses.size}")
                val verseList = mutableListOf<String>()
                for (currentVerse in verses) {
                    for (verse in currentVerse.translations) {
                        if (verse.language == "english") {
                            verseList.add(verse.description)
                            break
                        }
                    }
                }
                Log.d("VerseFragment", "Verse list size: ${verseList.size}")
                adapter.differ.submitList(verseList)
                binding.shimmer.visibility = View.GONE
                binding.rvVerse.visibility = View.VISIBLE
            })
        }
        else{
            val bundle = arguments
            binding.tvChapterName.text = bundle?.getString("chapterName")
            binding.tvChapterDescription.text = bundle?.getString("chapterSummary")
            binding.tvChapterNumber.text = "Chapter ${bundle?.getInt("chapterNumber", 0)}"
            binding.tvVerseCount.text = "Verses: ${bundle?.getInt("versesCount", 0)}"
            val chapterNum = bundle?.getInt("chapterNumber", 0)
            lifecycleScope.launch {
                viewModel.getSavedChapter(chapterNum!!)!!.observe(viewLifecycleOwner) { Chapter ->
                    val verseList = mutableListOf<String>()
                    for (verse in Chapter.verses) {
                        verseList.add(verse)
                    }

                    adapter.differ.submitList(verseList)
                    binding.shimmer.visibility = View.GONE
                    binding.rvVerse.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onVerseClicked(position: Int) {
        val databundle = arguments
        val chapterNumber = databundle?.getInt("chapterNumber", 0) ?: 0
        val bundle = Bundle().apply {
            putInt("chapterNumber", chapterNumber)
            putInt("verseNumber", position)
            putBoolean("callApi",true)
        }

        findNavController().navigate(R.id.action_verseFragment_to_verseDetailFragment, bundle)


    }
}