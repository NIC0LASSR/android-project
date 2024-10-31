import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.misfinanzas.R
import com.example.misfinanzas.models.FinancialTransaction
import com.example.misfinanzas.models.Transaction_State
import com.example.misfinanzas.screens.TransactionState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransaccionesScreen(
    transactionState: TransactionState,
    balance: Double,
    transactions: List<FinancialTransaction>,
    onDateSelected: (Long) -> Unit,
    onSaveClicked: (Long, String, String, String) -> Unit,
    onCancelClicked: () -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var amount by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf("Ingreso") }
    var selectedCategory by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(color = colorResource(id = R.color.gray_background))
    ) {
        Text(text = "Saldo Actual: $balance", style = MaterialTheme.typography.titleLarge)

        // Implementación de la selección de fecha
        DatePickerField(
            selectedDate = selectedDate?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) } ?: "",
            onDateSelected = { date ->
                selectedDate = date
                onDateSelected(date)
            }
        )

        // Campo para ingresar monto
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(stringResource(R.string.monto)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de tipo de transacción
        Row {
            RadioButton(selected = transactionType == "Ingreso", onClick = { transactionType = "Ingreso" })
            Text("Ingreso")
            RadioButton(selected = transactionType == "Gasto", onClick = { transactionType = "Gasto" })
            Text("Gasto")
        }

        // Botones para guardar y cancelar
        Row {
            Button(onClick = { onSaveClicked(selectedDate ?: 0L, amount, transactionType, selectedCategory) }) {
                Text("Guardar")
            }
            Button(onClick = onCancelClicked) {
                Text("Cancelar")
            }
        }

        // Listado de transacciones
        LazyColumn {
            items(transactions) { transaction ->
                Text(transaction.toString())
            }
        }

        // Mensaje de error o estado de carga
        if (transactionState is TransactionState.Error) {
            Text(transactionState.message, color = Color.Red)
        }
    }
}
@Composable
fun DatePickerField(selectedDate: String, onDateSelected: (Long) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    OutlinedButton(onClick = {
        // Crear y mostrar el DatePickerDialog
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Establecer la fecha seleccionada
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.timeInMillis) // Llamar a onDateSelected con la fecha
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }) {
        Text(text = if (selectedDate.isEmpty()) stringResource(R.string.seleccionar_fecha) else selectedDate)
    }
}
