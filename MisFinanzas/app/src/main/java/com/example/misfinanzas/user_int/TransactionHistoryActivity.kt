package com.example.misfinanzas.user_int
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.misfinanzas.R
import com.example.misfinanzas.viewmodel.TransactionViewModel

class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acvity_historial)

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.rvTransactions)
        adapter = TransactionsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        // Observar las transacciones y actualizarlas en el RecyclerView
        viewModel.allTransactions.observe(this, Observer { transactions ->
            adapter.setTransactions(transactions)
        })
    }
}