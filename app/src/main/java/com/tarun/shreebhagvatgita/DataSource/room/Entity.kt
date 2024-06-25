package com.tarun.shreebhagvatgita.DataSource.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tarun.shreebhagvatgita.models.Commentary
import com.tarun.shreebhagvatgita.models.Translation


@Entity(tableName = "SavedChapter")
    data class SavedChapter(
        val chapter_number: Int,
        val chapter_summary: String,
        val chapter_summary_hindi: String,
        @PrimaryKey
        val id: Int,
        val name: String,
        val name_meaning: String,
        val name_translated: String,
        val name_transliterated:String,
        val slug:String,
        val verses_count:Int,
        val verses:List<String>
    )

    @Entity(tableName = "SavedVerse")
    data class SavedVerse(
        val chapter_number: Int,
        val verse_number: Int,
        val text: String,
        val word_meanings: String,
        @PrimaryKey
        val id: Int,
        val translations: List<String>,
        val author_translation: List<String>,
        val commentaries: List<String>,
        val commentary_author: List<String>,
        val transliteration: String,
        val slug:String,
        )