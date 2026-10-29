package com.example.practicaindividualmati.data.local

import com.example.practicaindividualmati.data.aEntidad
import com.example.practicaindividualmati.data.aResumen
import com.example.practicaindividualmati.data.remote.SqliteCloudDataSource
import com.example.practicaindividualmati.models.KanyeQuoteResumen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritosRepository(
    private val dao: FavoritoDao,
    private val sqliteCloudDataSource: SqliteCloudDataSource = SqliteCloudDataSource()
) {
    fun observarFavoritos(): Flow<List<KanyeQuoteResumen>> =
        dao.observarFavoritos().map { entidades -> entidades.map { it.aResumen() } }

    fun observarIds(): Flow<Set<Int>> =
        dao.observarIdsFavoritos().map { ids -> ids.toSet() }

    suspend fun agregar(cita: KanyeQuoteResumen) = withContext(Dispatchers.IO) {
        val entidad = cita.aEntidad()
        dao.insertar(entidad)
        // Sincronizar automáticamente con SQLite Cloud (tabla 'quotes')
        try {
            sqliteCloudDataSource.guardarFavorito(entidad.id, entidad.quote, entidad.agregadoEn)
        } catch (e: Exception) {
            android.util.Log.e("FavoritosRepository", "Error sincronizando con SQLite Cloud: ${e.message}")
        }
    }

    suspend fun quitar(id: Int) = withContext(Dispatchers.IO) {
        dao.eliminarPorId(id)
        try {
            sqliteCloudDataSource.eliminarFavorito(id)
        } catch (e: Exception) {
            android.util.Log.e("FavoritosRepository", "Error eliminando de SQLite Cloud: ${e.message}")
        }
    }
}
