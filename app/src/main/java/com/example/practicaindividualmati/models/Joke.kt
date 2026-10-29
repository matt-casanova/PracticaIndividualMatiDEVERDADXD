package com.example.practicaindividualmati.models

/**
 * Data class que representa un chiste de Chuck Norris,
 * basada en la estructura del diagrama UML.
 */
data class Joke(
    val id: String,
    val value: String,
    val url: String,
    val icon_url: String,
    val categories: List<String>,
    // Usamos String para DateTime para facilitar el mapeo de la API ("2020-01-05 13:42:18.823766")
    val created_at: String, 
    val updated_at: String
)