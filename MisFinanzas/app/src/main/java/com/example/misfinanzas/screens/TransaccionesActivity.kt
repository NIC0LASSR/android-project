package com.example.misfinanzas.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.misfinanzas.R
import com.example.misfinanzas.database.AppDatabase
import com.example.misfinanzas.databinding.ActivityTransaccionBinding
import com.example.misfinanzas.models.TransactionState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransaccionesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransaccionBinding
    private var selectedDate: Long = 0
    private val transactionAdapter = TransactionAdapter()

    private val viewModel: TransaccionesViewModel by viewModels {
        TransaccionesViewModelFactory(AppDatabase.getDatabase(this).transactionDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaccionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        setupDatePicker()
        setupSpinner()
        setupButtons()
        setupRadioGroup()
        setupRecyclerView()
        setupSwipeToRefresh()
    }

    private fun setupDatePicker() {
        binding.editFecha.setOnClickListener {
            val calendar = Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.timeInMillis
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.editFecha.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupSpinner() {
        binding.spinnerCategoria.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun setupButtons() {
        binding.buttonAceptar.setOnClickListener {
            guardarTransaccion()
        }

        binding.buttonCancelar.setOnClickListener {
            finish()
        }
    }

    private fun setupRadioGroup() {
        binding.radioGroupTipo.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbIncome -> {
                    binding.rbIncome.setTextColor(getColor(R.color.verde))
                    binding.rbExpense.setTextColor(getColor(R.color.gray_primary))
                }

                R.id.rbExpense -> {
                    binding.rbExpense.setTextColor(getColor(R.color.rojo))
                    binding.rbIncome.setTextColor(getColor(R.color.gray_primary))
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvTransactions.apply {
            layoutManager = LinearLayoutManager(this@TransaccionesActivity)
            adapter = transactionAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@TransaccionesActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadTransactions()
        }
    }

    private fun guardarTransaccion() {
        if (selectedDate == 0L) {
            mostrarError("Debe seleccionar una fecha")
            return
        }

        val montoStr = binding.editMonto.text.toString()
        if (montoStr.isEmpty()) {
            mostrarError("Debe ingresar un monto")
            return
        }

        val tipo = when (binding.radioGroupTipo.checkedRadioButtonId) {
            R.id.rbIncome -> "Ingreso"
            R.id.rbExpense -> "Gasto"
            else -> {
                mostrarError("Debe seleccionar un tipo de transacción")
                return
            }
        }

        val categoria = binding.spinnerCategoria.selectedItem?.toString() ?: run {
            mostrarError("Debe seleccionar una categoría")
            return
        }

        // Convertir el monto a número y cambiar el signo si es un gasto
        val monto = montoStr.toDouble()
        val montoFinal = if (tipo == "Gasto") -monto else monto

        viewModel.insertTransaction(
            date = selectedDate,
            amount = montoFinal.toString(),
            type = tipo,
            category = categoria
        )


        limpiarCampos()

    }

    private fun observeViewModel() {
        // Observar estado de la transacción
        lifecycleScope.launch {
            viewModel.transactionState.collect { state ->
                handleTransactionState(state)
            }
        }

        // Observar balance
        lifecycleScope.launch {
            viewModel.balance.collect { balance ->
                updateBalance(balance)
            }
        }

        // Observar transacciones
        lifecycleScope.launch {
            viewModel.transactions.collect { transactions ->
                binding.swipeRefreshLayout.isRefreshing = false
                transactionAdapter.updateTransactions(transactions)
                updateEmptyState(transactions.isEmpty())
            }
        }
    }

    private fun handleTransactionState(state: TransactionState) {
        when (state) {
            is TransactionState.Loading -> {
                mostrarCargando(true)
                ocultarError()
            }

            is TransactionState.Success -> {
                mostrarCargando(false)
                limpiarCampos()
                ocultarError()
                Toast.makeText(
                    this,
                    "Transacción guardada exitosamente",
                    Toast.LENGTH_SHORT
                ).show()


                viewModel.onCancelTransaction()
            }

            is TransactionState.Error -> {
                mostrarCargando(false)
                mostrarError(state.message)
            }

            is TransactionState.Idle -> {
                mostrarCargando(false)
                ocultarError()
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun updateBalance(balance: Double) {
        val formattedBalance = String.format("$%,.2f", balance)
        binding.tvBalance.text = getString(R.string.saldo_actual_formato, formattedBalance)
        binding.tvBalance.setTextColor(getColor(if (balance >= 0) R.color.verde else R.color.rojo))
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.tvEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvTransactions.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun mostrarCargando(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.buttonAceptar.isEnabled = !show
        binding.buttonCancelar.isEnabled = !show
    }

    private fun mostrarError(mensaje: String) {
        binding.tvError.apply {
            text = mensaje
            visibility = View.VISIBLE
        }
    }

    private fun ocultarError() {
        binding.tvError.visibility = View.GONE
    }

    private fun limpiarCampos() {
        binding.editFecha.setText("")
        binding.editMonto.setText("")
        binding.radioGroupTipo.clearCheck()
        binding.spinnerCategoria.setSelection(0)
        selectedDate = 0
    }
}
