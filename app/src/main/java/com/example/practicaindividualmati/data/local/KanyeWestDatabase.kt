package com.example.practicaindividualmati.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.java

@Database(entities = [FavoritoEntity::class], version = 1, exportSchema = false)
abstract class KanyeWestDatabase : RoomDatabase() {
    abstract fun favoritoDao(): FavoritoDao
    companion object {
        @Volatile
        private var instancia: KanyeWestDatabase? = null
        // Singleton: una sola instancia de la base de datos en toda la app.
        fun obtener(context: Context): KanyeWestDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    KanyeWestDatabase::class.java,
                    "kanyex.db"
                ).build().also { instancia = it }
            }
    }
}