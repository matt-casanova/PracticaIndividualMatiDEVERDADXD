package com.example.practicaindividualmati.data.remote

import android.util.Log
import com.example.practicaindividualmati.BuildConfig
import com.example.practicaindividualmati.data.RetrofitClient

/**
 * Fuente de datos remota para ejecutar consultas SQL en SQLite Cloud para sincronizar las citas de Kanye West.
 */
class SqliteCloudDataSource(
    private val api: SqliteCloudApi = RetrofitClient.sqliteCloudApi
) {
    private val candidateUrls = listOf(
        "https://cwchoov3dz.g1.sqlite.cloud/sql",
        "https://cwchoov3dz.g1.sqlite.cloud/v1/sql",
        "https://cwchoov3dz.g1.sqlite.cloud/",
        "https://cwchoov3dz.sqlite.cloud/sql",
        "https://cwchoov3dz.sqlite.cloud/v1/sql",
        "https://cwchoov3dz.sqlite.cloud/",
        "https://cwchoov3dz.g1.gateway.sqlite.cloud/sql",
        "https://cwchoov3dz.g1.gateway.sqlite.cloud/v1/sql",
        "https://cwchoov3dz.g1.gateway.sqlite.cloud/"
    )
    private var workingUrl: String = "https://cwchoov3dz.g1.sqlite.cloud/sql"
    private val databaseName: String = "priemerDB.sqlite"

    /**
     * Sincroniza/Guarda una cita de Kanye West en la tabla 'quotes' de SQLite Cloud.
     */
    suspend fun guardarFavorito(id: Int, quote: String, agregadoEn: Long): Boolean {
        val apiKey = BuildConfig.SQlite_KEY
        if (apiKey.isBlank()) {
            Log.e("SqliteCloud", "SQlite_KEY está vacía en local.properties o BuildConfig")
            return false
        }

        val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
        
        // Asegurar que la tabla 'quotes' existe con las columnas id y quote
        val createTableSql = "CREATE TABLE IF NOT EXISTS quotes (id INTEGER PRIMARY KEY, quote TEXT);"
        val insertSql = "INSERT OR REPLACE INTO quotes (id, quote) VALUES ($id, '${quote.replace("'", "''")}');"
        val fullSql = "USE DATABASE $databaseName; $createTableSql $insertSql"

        val requestPayload = SqliteCloudRequest(database = databaseName, sql = fullSql)

        val urlsToTry = listOf(workingUrl) + candidateUrls.filter { it != workingUrl }

        for (url in urlsToTry) {
            try {
                Log.d("SqliteCloud", "Probando URL SQLite Cloud: $url")
                val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                if (resp.isSuccessful) {
                    workingUrl = url
                    Log.d("SqliteCloud", "sqlite EXITO")
                    return true
                } else {
                    Log.w("SqliteCloud", "Fallo HTTP ${resp.code()} en $url: ${resp.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.w("SqliteCloud", "Excepción con $url: ${e.message}")
            }
        }

        Log.e("SqliteCloud", "Todos los endpoints de SQLite Cloud fallaron (404/Error)")
        return false
    }

    /**
     * Elimina una cita de SQLite Cloud por su ID.
     */
    suspend fun eliminarFavorito(id: Int): Boolean {
        val apiKey = BuildConfig.SQlite_KEY
        if (apiKey.isBlank()) return false

        val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
        val sql = "USE DATABASE $databaseName; DELETE FROM quotes WHERE id = $id;"
        val requestPayload = SqliteCloudRequest(database = databaseName, sql = sql)

        val urlsToTry = listOf(workingUrl) + candidateUrls.filter { it != workingUrl }

        for (url in urlsToTry) {
            try {
                val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                if (resp.isSuccessful) {
                    workingUrl = url
                    Log.d("SqliteCloud", "sqlite EXITO")
                    return true
                }
            } catch (e: Exception) {
                // ignore
            }
        }
        return false
    }
}
