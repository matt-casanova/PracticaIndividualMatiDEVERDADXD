package com.example.practicaindividualmati.data.stoic

import retrofit2.http.GET;

interface StoicApi {

    @GET("stoic-quote")
        suspend fun obtenerCita(): StoicResponseDto
}