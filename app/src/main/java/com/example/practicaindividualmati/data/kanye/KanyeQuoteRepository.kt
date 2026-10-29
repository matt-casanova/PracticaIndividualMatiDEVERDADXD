package com.example.practicaindividualmati.data.kanye

import com.example.practicaindividualmati.data.RetrofitClient

class KanyeQuoteRepository {
    suspend fun obtenerCItaKanyeAleatoria(): KanyeQuoteDto {
        return RetrofitClient.instanceKanye.getRandomKanyeQuote()
    }
}