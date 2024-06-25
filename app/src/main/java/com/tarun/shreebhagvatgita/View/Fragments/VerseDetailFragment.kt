package com.tarun.shreebhagvatgita.View.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.tarun.shreebhagvatgita.NetworManager
import com.tarun.shreebhagvatgita.ViewModel.MainViewModel
import com.tarun.shreebhagvatgita.databinding.FragmentVerseDetailBinding
import com.tarun.shreebhagvatgita.models.Commentary
import com.tarun.shreebhagvatgita.models.Translation
import com.tarun.shreebhagvatgita.models.VerseItems
import com.tarun.shreebhagvatgita.DataSource.room.SavedVerse
import kotlinx.coroutines.launch


class VerseDetailFragment : Fragment() {

    lateinit var binding: FragmentVerseDetailBinding
    private var chapterNumber: Int = 1
    private var verseNumber: Int = 1
    val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentVerseDetailBinding.inflate(inflater,container,false)

        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber", 0) ?: 0
        verseNumber = bundle?.getInt("verseNumber", 0) ?: 0
        Log.d("VerseDetailFragment", "Chapter Number: $chapterNumber , Verse Number: $verseNumber")
        checkInternet()
        onFavFilledClicked()
        onReadMoreClicked()
        runthroughsp()

        return binding.root
    }

    private fun runthroughsp() {
        lifecycleScope.launch {
            if(viewModel.getversechpsp().contains("${chapterNumber}.${verseNumber}")){
                binding.ivFavouritefilled.visibility=View.VISIBLE
                binding.ivfavourite.visibility=View.INVISIBLE
            }else{
                binding.ivFavouritefilled.visibility=View.INVISIBLE
                binding.ivfavourite.visibility=View.VISIBLE
            }
        }
    }

    private fun checkInternet() {
        val networkManager = NetworManager(requireContext())
        networkManager.observe(viewLifecycleOwner, Observer {
            if (it) {

                getdata()

            } else {
                val bundle = arguments
                if(!bundle!!.getBoolean("{callApi")){
                    getdata()
                }
                binding.progressBar.visibility = View.INVISIBLE
                binding.verseContainer.visibility = View.INVISIBLE
                binding.iv.visibility=View.VISIBLE
                binding.llnicheka.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
            }
        })
    }

    fun getdata() {
        if(arguments?.getBoolean("callApi") == true) {
            lifecycleScope.launch {
                viewModel.getVerse(chapterNumber, verseNumber)
                    .observe(viewLifecycleOwner, Observer { verse ->
                        binding.tvVerseText.text = verse.text
                        binding.tvVerseTextEnglish.text = verse.transliteration
                        binding.tvVerseTextEnglishWord.text = verse.word_meanings
                        binding.tvVerseNumber.text =
                            "||${verse.chapter_number}.${verse.verse_number}||"

                        val verseList = arrayListOf<Translation>()
                        for (translation in verse.translations) {
                            if (translation.language == "english" || translation.language == "hindi") {
                                verseList.add(translation)
                            }
                        }
                        binding.tvtranslation.text = verseList[0].description
                        binding.tvauthorname.text = verseList[0].author_name

                        if (verseList.size == 1) {
                            binding.fabtranslationRight.visibility = View.GONE
                        }

                        var i = 0
                        binding.fabtranslationRight.setOnClickListener {
                            if (i < verseList.size - 1) {
                                i++
                                binding.tvtranslation.text = verseList[i].description
                                binding.fabtranslationRight.visibility = View.VISIBLE
                                binding.tvauthorname.text = verseList[i].author_name
                                binding.fabtranslationLeft.visibility = View.VISIBLE
                            } else {
                                binding.fabtranslationRight.visibility = View.GONE
                                binding.fabtranslationLeft.visibility = View.VISIBLE
                            }
                        }

                        binding.fabtranslationLeft.setOnClickListener {
                            if (i >= 1) {
                                i--
                                binding.tvtranslation.text = verseList[i].description
                                binding.fabtranslationRight.visibility = View.VISIBLE
                                binding.tvauthorname.text = verseList[i].author_name
                                binding.fabtranslationLeft.visibility = View.VISIBLE
                            } else {
                                binding.fabtranslationLeft.visibility = View.GONE
                                binding.fabtranslationRight.visibility = View.VISIBLE
                            }
                        }

                        val commentaryList = arrayListOf<Commentary>()
                        for (commentary in verse.commentaries) {
                            if (commentary.language == "english" || commentary.language == "hindi") {
                                commentaryList.add(commentary)
                            }
                        }
                        binding.tvcommentary.text = commentaryList[0].description
                        binding.authorname.text = commentaryList[0].author_name

                        if (commentaryList.size == 1) { // Adjusted to commentaryList
                            binding.translationRight.visibility = View.GONE
                        }

                        var j = 0
                        binding.translationRight.setOnClickListener {
                            if (j < commentaryList.size - 1) { // Adjusted to commentaryList
                                j++
                                binding.tvcommentary.text = commentaryList[j].description
                                binding.authorname.text = commentaryList[j].author_name
                                binding.translationRight.visibility = View.VISIBLE
                                binding.translationLeft.visibility = View.VISIBLE
                            } else {
                                binding.translationRight.visibility = View.GONE
                                binding.translationLeft.visibility = View.VISIBLE
                            }
                        }

                        binding.translationLeft.setOnClickListener {
                            if (j > 0) { // Corrected to 0
                                j--
                                binding.tvcommentary.text = commentaryList[j].description
                                binding.authorname.text = commentaryList[j].author_name
                                binding.translationRight.visibility = View.VISIBLE
                                binding.translationLeft.visibility = View.VISIBLE
                            } else {
                                binding.translationLeft.visibility = View.GONE
                                binding.translationRight.visibility = View.VISIBLE
                            }
                        }

                        binding.progressBar.visibility = View.GONE
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.verseContainer.visibility = View.VISIBLE
                        binding.llnicheka.visibility = View.VISIBLE
                        binding.tvShowingMessage.visibility = View.GONE

                        val translationList = mutableListOf<String>()
                        val authorList = mutableListOf<String>()
                        for (translation in verseList) {
                            translationList.add(translation.description)
                            authorList.add(translation.author_name)
                        }
                        val commentarylist = mutableListOf<String>()
                        val commentaryAuthor = mutableListOf<String>()
                        for (commentaries in commentaryList) {
                            commentarylist.add(commentaries.description)
                            commentaryAuthor.add(commentaries.author_name)
                        }

                        binding.ivfavourite.setOnClickListener {
                            lifecycleScope.launch {
                                viewModel.addToVerseSharedPrefrence(
                                    "${chapterNumber}.${verseNumber}",
                                    verseNumber
                                )
                            }
                            binding.ivfavourite.visibility = View.INVISIBLE
                            binding.ivFavouritefilled.visibility = View.VISIBLE
                            saveVerse(
                                verse,
                                translationList,
                                authorList,
                                commentarylist,
                                commentaryAuthor
                            )
                        }
                    })
            }
        }
        else{
            val bundle=arguments
            val cno=bundle?.getInt("chapterNumber")
            val vno=bundle?.getInt("verseNumber")
            viewModel.getSavedVerse(cno!!,vno!!)?.observe(viewLifecycleOwner){verse ->
                binding.tvVerseText.text = verse.text
                binding.tvVerseTextEnglish.text = verse.transliteration
                binding.tvVerseTextEnglishWord.text = verse.word_meanings
                binding.tvVerseNumber.text =
                    "||${verse.chapter_number}.${verse.verse_number}||"


                binding.tvtranslation.text = verse.translations[0]
                binding.tvauthorname.text = verse.author_translation[0]

                if (verse.translations.size == 1) {
                    binding.fabtranslationRight.visibility = View.GONE
                }

                var i = 0
                binding.fabtranslationRight.setOnClickListener {
                    if (i < verse.translations.size - 1) {
                        i++
                        binding.tvtranslation.text = verse.translations[i]
                        binding.fabtranslationRight.visibility = View.VISIBLE
                        binding.tvauthorname.text = verse.author_translation[i]
                        binding.fabtranslationLeft.visibility = View.VISIBLE
                    } else {
                        binding.fabtranslationRight.visibility = View.GONE
                        binding.fabtranslationLeft.visibility = View.VISIBLE
                    }
                }

                binding.fabtranslationLeft.setOnClickListener {
                    if (i >= 1) {
                        i--
                        binding.tvtranslation.text = verse.translations[i]
                        binding.fabtranslationRight.visibility = View.VISIBLE
                        binding.tvauthorname.text = verse.author_translation[i]
                        binding.fabtranslationLeft.visibility = View.VISIBLE
                    } else {
                        binding.fabtranslationLeft.visibility = View.GONE
                        binding.fabtranslationRight.visibility = View.VISIBLE
                    }
                }


                binding.tvcommentary.text = verse.commentaries[0]
                binding.authorname.text = verse.commentary_author[0]

                if (verse.commentaries.size == 1) { // Adjusted to commentaryList
                    binding.translationRight.visibility = View.GONE
                }

                var j = 0
                binding.translationRight.setOnClickListener {
                    if (j < verse.commentaries.size - 1) { // Adjusted to commentaryList
                        j++
                        binding.tvcommentary.text = verse.commentaries[j]
                        binding.authorname.text = verse.commentary_author[j]
                        binding.translationRight.visibility = View.VISIBLE
                        binding.translationLeft.visibility = View.VISIBLE
                    } else {
                        binding.translationRight.visibility = View.GONE
                        binding.translationLeft.visibility = View.VISIBLE
                    }
                }

                binding.translationLeft.setOnClickListener {
                    if (j > 0) { // Corrected to 0
                        j--
                        binding.tvcommentary.text = verse.commentaries[j]
                        binding.authorname.text = verse.commentary_author[j]
                        binding.translationRight.visibility = View.VISIBLE
                        binding.translationLeft.visibility = View.VISIBLE
                    } else {
                        binding.translationLeft.visibility = View.GONE
                        binding.translationRight.visibility = View.VISIBLE
                    }
                }

                binding.progressBar.visibility = View.GONE
                binding.progressBar.visibility = View.INVISIBLE
                binding.verseContainer.visibility = View.VISIBLE
                binding.llnicheka.visibility = View.VISIBLE
                binding.tvShowingMessage.visibility = View.GONE

            }



        }
    }

    private fun saveVerse(
        verse: VerseItems?,
        translationList: MutableList<String>,
        authorList: MutableList<String>,
        commentarylist: MutableList<String>,
        commentaryAuthor: MutableList<String>
    ) {
        val savedVerse= SavedVerse(
            verse!!.chapter_number,
            verse!!.verse_number
            ,verse.text,verse.word_meanings,
        verse.id,translationList,authorList,commentarylist,commentaryAuthor,verse.transliteration,verse.slug)
        lifecycleScope.launch {
            viewModel.insertSavedVerse(savedVerse)
        }

    }

    private fun onFavFilledClicked(){
        binding.ivFavouritefilled.setOnClickListener {
            binding.ivFavouritefilled.visibility = View.INVISIBLE
            binding.ivfavourite.visibility = View.VISIBLE
            lifecycleScope.launch {
                viewModel.deleteSavedVerse(chapterNumber, verseNumber)
            }
        }
    }




    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.tvseemore.setOnClickListener {
            isExpanded = if (!isExpanded) {
                binding.tvcommentary.maxLines = 20
                true
            } else {
                binding.tvcommentary.maxLines = 4
                false
            }
        }
    }
}
