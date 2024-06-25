package com.tarun.shreebhagvatgita.models

data class ParticularVerse(
    val chapter_number: Int,
    val verse_number: Int,
    val text: String,
    val word_meanings: String,
    val id: Int,
    val translations: List<Translation>,
    val commentaries: List<Commentary>,
    val transliteration: String,
    val slug:String,

)