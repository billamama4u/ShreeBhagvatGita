package com.tarun.shreebhagvatgita.View.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tarun.shreebhagvatgita.R
import com.tarun.shreebhagvatgita.View.adapters.ChapterAdapter
import com.tarun.shreebhagvatgita.ViewModel.MainViewModel
import com.tarun.shreebhagvatgita.databinding.FragmentSavedChapterBinding
import com.tarun.shreebhagvatgita.models.ChapterItems
import kotlinx.coroutines.launch


class SavedChapterFragment : Fragment() {
    private lateinit var binding: FragmentSavedChapterBinding

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ChapterAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedChapterBinding.inflate(layoutInflater)


        getChp()
        return binding.root

    }

    private fun getChp() {
        lifecycleScope.launch {
            viewModel.getAllSavedChapters()!!.observe(viewLifecycleOwner) { savedChapters ->
                val chapterlist = arrayListOf<ChapterItems>()

                for (savedChapter in savedChapters) {
                    val chapter = ChapterItems(
                        savedChapter.chapter_number,
                        savedChapter.chapter_summary,
                        savedChapter.chapter_summary_hindi,
                        savedChapter.id,
                        savedChapter.name,
                        savedChapter.name_meaning,
                        savedChapter.name_translated,
                        savedChapter.name_transliterated,
                        savedChapter.slug,
                        savedChapter.verses_count
                    )
                    chapterlist.add(chapter)
                }
                if (chapterlist.isNotEmpty()) {
                    adapter = ChapterAdapter(
                        ::onVerseClicked,
                        ::onFavourite4Clicked,
                        false,
                        ::onFillheartClicked,
                        viewModel
                    )
                    binding.rvChapters.adapter = adapter
                    binding.shimmer.visibility=View.GONE
                    binding.rvChapters.visibility=View.VISIBLE
                    adapter.differ.submitList(chapterlist)
                }else{
                    binding.tvShowingMessage.visibility=View.VISIBLE
                    binding.shimmer.visibility=View.GONE
                    binding.rvChapters.visibility=View.GONE
                }
            }

        }
    }
    private fun onFillheartClicked(chapterItems: ChapterItems){

    }

    private fun onVerseClicked(chapterItems: ChapterItems) {
        val bundle = Bundle().apply {
            putString("chapterName", chapterItems.name_translated)
            putBoolean("callApi",false)
            putString("chapterSummary", chapterItems.chapter_summary)
            putInt("chapterNumber", chapterItems.chapter_number)
            putInt("versesCount", chapterItems.verses_count)
        }
        findNavController().navigate(R.id.action_savedChapterFragment_to_verseFragment, bundle)

    }
    private fun onFavourite4Clicked(chapterItems: ChapterItems) {

    }

}
