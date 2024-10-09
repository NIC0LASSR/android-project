package com.example.misfinanzas.user_int

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misfinanzas.R
import com.example.misfinanzas.viewmodel.TransactionViewModel

class CurrentBalanceActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var tvBalance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activy_saldo_actual)

        // Inicializar las vistas
        tvBalance = findViewById(R.id.tvBalance)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        // Observar el saldo total
        viewModel.totalBalance.observe(this, Observer { balance ->
            tvBalance.text = "Saldo Actual: $$balance"
        })
    }
}