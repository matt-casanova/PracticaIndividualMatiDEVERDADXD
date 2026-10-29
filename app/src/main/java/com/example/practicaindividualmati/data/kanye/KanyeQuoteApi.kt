package com.example.practicaindividualmati.data.kanye

import retrofit2.http.GET

interface KanyeQuoteApi {
    @GET ("/")
    suspend fun getRandomKanyeQuote(): KanyeQuoteDto
}