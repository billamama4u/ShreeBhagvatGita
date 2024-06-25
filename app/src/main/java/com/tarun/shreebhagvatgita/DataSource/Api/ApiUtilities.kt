package com.tarun.shreebhagvatgita.DataSource.Api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    val headers = mapOf<String, String>(
        "Accept" to  "application/json",
        "x-rapidapi-key" to "b368213afdmsh0a5d5edb0b940bfp1820e4jsn129bd81caf5e",
        "x-rapidapi-host" to "bhagavad-gita3.p.rapidapi.com"
    )

    val client = OkHttpClient.Builder().apply {
        addInterceptor{
            val request = it.request()
                .newBuilder().apply {
                headers.forEach{ (key, value) -> addHeader(key,value)}}
                .build()
            it.proceed(request)
        }
    }.build()

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://bhagavad-gita3.p.rapidapi.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}