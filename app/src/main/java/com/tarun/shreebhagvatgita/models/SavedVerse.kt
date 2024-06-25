package com.tarun.shreebhagvatgita.models

data class SavedVerse(
    val chapter_number: Int,
    val verse_number: Int,
    val text: String,
    val word_meanings: String,
    val id: Int,
    val translations: List<String>,
    val author_translation: List<String>,
    val commentaries: List<String>,
    val commentary_author: List<String>,
    val transliteration: String,
    val slug:String,
)