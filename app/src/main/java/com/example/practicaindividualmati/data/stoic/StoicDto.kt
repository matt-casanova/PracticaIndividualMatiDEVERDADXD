package com.example.practicaindividualmati.data.stoic

import com.google.gson.annotations.SerializedName

// 1. Este es el objeto principal que envuelve toda la respuesta
data class StoicResponseDto(
    @SerializedName("data") val data: StoicDto
)

// 2. Este es el objeto interno que ya tenías
data class StoicDto (
    @SerializedName("author") val author: String,
    @SerializedName("quote") val quote: String
)