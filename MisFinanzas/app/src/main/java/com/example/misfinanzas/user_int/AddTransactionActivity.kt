package com.example.misfinanzas.user_int
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.misfinanzas.R
import com.example.misfinanzas.models.Transaction
import com.example.misfinanzas.viewmodel.TransactionViewModel

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var etAmount: EditText
    private lateinit var spCategory: Spinner
    private lateinit var rgType: RadioGroup
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activy_ingresos)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        // Inicializar las vistas
        etAmount = findViewById(R.id.editMonto)
        spCategory = findViewById(R.id.spinnerCategoria)
        rgType = findViewById(R.id.radioGroupTipo)
        btnSave = findViewById(R.id.buttonAceptar)

        // Configurar el Spinner de categorías
        val categories = listOf("Comida", "Transporte", "Entretenimiento", "Salud", "Otros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapter

        // Configurar el botón de guardar
        btnSave.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val category = spCategory.selectedItem.toString()
            val type = if (rgType.checkedRadioButtonId == R.id.rbIncome) "Ingreso" else "Gasto"
            val currentTimeMillis = System.currentTimeMillis()

            // Crear la transacción y guardarla en la base de datos
            val transaction = Transaction(
                amount = amount,
                category = category,
                date = currentTimeMillis,
                type = type
            )
            viewModel.insert(transaction)
            finish() // Finaliza la actividad después de guardar la transacción
        }
    }
}
