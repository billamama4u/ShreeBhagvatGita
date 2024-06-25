package com.tarun.shreebhagvatgita.DataSource.Api


import com.tarun.shreebhagvatgita.models.ChapterItems
import com.tarun.shreebhagvatgita.models.VerseItems
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {


    @GET("/v2/chapters/?skip=0&limit=18")
    suspend fun getChapters(): List<ChapterItems>

    @GET("/v2/chapters/{chapter_number}/verses/")
    suspend fun getVerses(@Path("chapter_number") chapterNumber: Int): List<VerseItems>

    @GET("/v2/chapters/{chapter_number}/verses/{verse_number}/")
    suspend fun getVerse(@Path("chapter_number") chapterNumber: Int, @Path("verse_number") verseNumber: Int): VerseItems

}

///