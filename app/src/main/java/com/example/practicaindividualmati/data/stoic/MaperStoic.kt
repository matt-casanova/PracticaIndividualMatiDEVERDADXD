package com.example.practicaindividualmati.data.stoic
import com.example.practicaindividualmati.models.StoicQuote

fun StoicDto.toDomain(): StoicQuote {
    return StoicQuote(
        author = this.author ?: "",
        qoute = this.quote ?: ""
    )
}
