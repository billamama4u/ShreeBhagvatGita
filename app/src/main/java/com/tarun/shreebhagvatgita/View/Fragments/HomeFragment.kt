package com.tarun.shreebhagvatgita.View.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tarun.shreebhagvatgita.NetworManager
import com.tarun.shreebhagvatgita.R
import com.tarun.shreebhagvatgita.View.adapters.ChapterAdapter
import com.tarun.shreebhagvatgita.ViewModel.MainViewModel
import com.tarun.shreebhagvatgita.databinding.FragmentHomeBinding
import com.tarun.shreebhagvatgita.models.ChapterItems
import com.tarun.shreebhagvatgita.DataSource.room.SavedChapter
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: ChapterAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(layoutInflater)
        statusbarcolour()
        checkInternet()

        showingVerseOfTheDay()


        binding.ivSettings.setOnClickListener {
            onSettingsClicked()
        }


        return  binding.root
    }

    private fun showingVerseOfTheDay() {
        val chapterNumber=(1..18).random()
        val verseNumber=(1..20).random()
        lifecycleScope.launch {
            viewModel.getVerse(chapterNumber,verseNumber).observe(viewLifecycleOwner){
                for(i in it.translations){
                    if(i.language == "english"){
                        binding.tvVerseOfTheDay.text=i.description
                        break
                    }
                }
            }
        }
    }

    private fun onFavourite4Clicked(chapterItems: ChapterItems) {
        lifecycleScope.launch {
            viewModel.addToSharedPrefrence(chapterItems.chapter_number.toString(),chapterItems.id)
            viewModel.fetchVerses(chapterItems.chapter_number)
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
                val savedChapter = SavedChapter(
                    chapter_number = chapterItems.chapter_number,
                    chapter_summary = chapterItems.chapter_summary,
                    chapter_summary_hindi = chapterItems.chapter_summary_hindi,
                    id = chapterItems.id,
                    name = chapterItems.name,
                    name_meaning = chapterItems.name_meaning,
                    name_translated = chapterItems.name_translated,
                    verses_count = chapterItems.verses_count,
                    verses = verseList,
                    slug = chapterItems.slug,
                    name_transliterated = chapterItems.name_transliterated
                )
                lifecycleScope.launch {
                    viewModel.inserChapter(savedChapter)
                }
            })
        }
    }

    private fun checkInternet() {
        val networkManager = NetworManager(requireContext())
        networkManager.observe(viewLifecycleOwner , Observer{
            if(it== true){
                binding.shimmer.visibility=View.VISIBLE
                binding.rvChapters.visibility=View.GONE
                binding.tvShowingMessage.visibility=View.GONE
                getAllChapters()
            }
            else{
                binding.shimmer.visibility=View.GONE
                binding.rvChapters.visibility=View.GONE
                binding.tvShowingMessage.visibility=View.VISIBLE
            }
        })
    }

    private fun getAllChapters() {
        viewModel.chapter.observe(viewLifecycleOwner, Observer { chapters ->
            adapter = ChapterAdapter(::onVerseClicked, ::onFavourite4Clicked, true,::onFillheartClicked,viewModel)
            binding.rvChapters.adapter = adapter
            adapter.differ.submitList(chapters)
            binding.shimmer.visibility=View.GONE
            binding.rvChapters.visibility=View.VISIBLE

        })
    }

    private fun onVerseClicked(chapterItems: ChapterItems){
        val bundle = Bundle().apply {
            putString("chapterName", chapterItems.name_translated)
            putBoolean("callApi",true)
            putString("chapterSummary", chapterItems.chapter_summary)
            putInt("chapterNumber", chapterItems.chapter_number)
            putInt("versesCount", chapterItems.verses_count)
        }
        findNavController().navigate(R.id.action_homeFragment_to_verseFragment, bundle)
    }

    private fun onSettingsClicked(){
        findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
    }

    private fun onFillheartClicked(chapterItems: ChapterItems) {
        lifecycleScope.launch {
            viewModel.removechpsp(chapterItems.chapter_number.toString())
            viewModel.deleteSavedChapter(chapterItems.id)
        }
    }

    private fun statusbarcolour(){
        activity?.window?.apply {
            val statusbarcolour = ContextCompat.getColor(requireContext(),R.color.white)
            statusBarColor= statusbarcolour
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}