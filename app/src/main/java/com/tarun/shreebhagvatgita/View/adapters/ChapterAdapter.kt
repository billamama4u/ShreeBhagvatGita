package com.tarun.shreebhagvatgita.View.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarun.shreebhagvatgita.ViewModel.MainViewModel
import com.tarun.shreebhagvatgita.databinding.ViewChaptersBinding
import com.tarun.shreebhagvatgita.models.ChapterItems

class ChapterAdapter(
    val onVerseClicked: (ChapterItems) -> Unit,
    val onFavouriteClicked: (ChapterItems) -> Unit,
    val room: Boolean,
    val onFillheartClicked: (ChapterItems) -> Unit,
    val viewModel: MainViewModel
) :RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    inner class ChapterViewHolder(val binding: ViewChaptersBinding) : RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<ChapterItems>(){
        override fun areItemsTheSame(oldItem: ChapterItems, newItem: ChapterItems): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChapterItems, newItem: ChapterItems): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        return ChapterViewHolder(ViewChaptersBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = differ.currentList[position]
        holder.binding.apply {
            tvChapterName.text = chapter.name_translated
            tvChpdescp.text = chapter.chapter_summary
            tvVerseCount.text = chapter.verses_count.toString()
            tvChapterNumber.text = buildString {
                append("Chapter ")
                append(chapter.chapter_number.toString())
            }
                ll.setOnClickListener {
                    onVerseClicked(chapter)
                }
            if(room) {
                if (viewModel.getchpsp().contains(chapter.chapter_number.toString())) {
                    favourite.visibility = View.GONE
                    favouritefilled.visibility = View.VISIBLE
                } else {
                    favourite.visibility = View.VISIBLE
                    favouritefilled.visibility = View.GONE
                }
            }
            if(!room){
                favourite.visibility = View.INVISIBLE
            }

            favourite.setOnClickListener {
                favourite.visibility=View.GONE
                onFavouriteClicked(chapter)
                favouritefilled.visibility=View.VISIBLE
            }
            favouritefilled.setOnClickListener {
                favouritefilled.visibility=View.GONE
                favourite.visibility=View.VISIBLE
                onFillheartClicked(chapter)
            }
        }

    }
}