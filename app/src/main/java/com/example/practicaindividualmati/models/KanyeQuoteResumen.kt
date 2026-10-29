package com.example.practicaindividualmati.models

data class KanyeQuoteResumen (
    val quote: String
) {
    fun aResumen(): KanyeQuoteResumen = KanyeQuoteResumen(quote = quote)
}