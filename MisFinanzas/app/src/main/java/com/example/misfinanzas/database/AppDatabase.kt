package com.example.misfinanzas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.misfinanzas.dao.TransactionDao
import com.example.misfinanzas.models.FinancialTransaction

// Define la base de datos Room para la aplicación, especificando las entidades y la versión
@Database(entities = [FinancialTransaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Método abstracto para obtener el DAO asociado
    abstract fun transactionDao(): TransactionDao

    companion object {
        // Variable para mantener una instancia de la base de datos única en toda la aplicación
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Método para obtener la instancia de la base de datos, creando una si no existe
        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia es nula, la crea dentro de un bloque sincronizado para asegurar un Singleton
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_database"
                ).build()
                // Asigna la nueva instancia a la variable global
                INSTANCE = instance
                instance
            }
        }
    }
}
