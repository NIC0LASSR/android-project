package com.example.misfinanzas

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.misfinanzas.dao.TransactionDao
import com.example.misfinanzas.database.AppDatabase
import com.example.misfinanzas.models.FinancialTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao

    @Before
    fun createDb() {
        // Crear una base de datos en memoria para pruebas
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        transactionDao = db.transactionDao()
    }

    @After
    fun closeDb() {
        // Verifica si db está inicializado antes de cerrarlo
        if (this::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun insertAndGetTransaction() = runBlocking {
        // Crear una transacción
        val transaction = FinancialTransaction(
            date = System.currentTimeMillis(),
            amount = 100.0,
            type = "Ingreso",
            category = "Salario"
        )
        // Insertar la transacción en la base de datos
        transactionDao.insertTransaction(transaction)

        // Obtener todas las transacciones y verificar que se ha insertado correctamente
        val allTransactions =
            transactionDao.getAllTransactions().first() // Recolectar el Flow en una lista
        assertEquals(1, allTransactions.size)
        assertEquals(transaction.amount, allTransactions[0].amount, 0.001)

    }
}
