package com.example.misfinanzas.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.misfinanzas.R
import com.example.misfinanzas.models.FinancialTransaction
import com.example.misfinanzas.models.TransactionState
import com.example.misfinanzas.ui.TransactionItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransaccionesScreen(
    transactionState: TransactionState,
    onDateSelected: (Long) -> Unit,
    onSaveClicked: (Long, String, String, String) -> Unit,
    onCancelClicked: () -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var amount by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf("Ingreso") }
    var selectedCategory by remember { mutableStateOf("") }

    // Efecto para limpiar los campos cuando el estado es Success
    LaunchedEffect(transactionState) {
        if (transactionState is TransactionState.Success) {
            clearFields(
                onDateClear = { selectedDate = null },
                onAmountClear = { amount = "" },
                onTypeClear = { transactionType = "Ingreso" },
                onCategoryClear = { selectedCategory = "" }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = colorResource(id = R.color.gray_background)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado de la transacción
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.header_transacction),
                contentDescription = "Header Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
        }

        // Selector de Fecha
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
        Column {
            Text(text = "Tipo de Transacción", style = MaterialTheme.typography.bodyMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = transactionType == "Ingreso",
                    onClick = { transactionType = "Ingreso" }
                )
                Text("Ingreso")
                RadioButton(
                    selected = transactionType == "Gasto",
                    onClick = { transactionType = "Gasto" }
                )
                Text("Gasto")
            }
        }

        // Selector de Categoría
        CategorySelector(selectedCategory) { selectedCategory = it }

        // Botones para guardar y cancelar
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    onSaveClicked(
                        selectedDate ?: 0L,
                        amount,
                        transactionType,
                        selectedCategory
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }
            OutlinedButton(
                onClick = {
                    clearFields(
                        onDateClear = { selectedDate = null },
                        onAmountClear = { amount = "" },
                        onTypeClear = { transactionType = "Ingreso" },
                        onCategoryClear = { selectedCategory = "" }
                    )
                    onCancelClicked()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
        }

        // Mensaje de error o estado de carga
        if (transactionState is TransactionState.Error) {
            Text(transactionState.message, color = Color.Red)
        }
    }
}

private fun clearFields(
    onDateClear: () -> Unit,
    onAmountClear: () -> Unit,
    onTypeClear: () -> Unit,
    onCategoryClear: () -> Unit
) {
    onDateClear()
    onAmountClear()
    onTypeClear()
    onCategoryClear()
}

@Composable
fun DatePickerField(selectedDate: String, onDateSelected: (Long) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    OutlinedButton(
        onClick = {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    onDateSelected(calendar.timeInMillis)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (selectedDate.isEmpty())
                stringResource(R.string.seleccionar_fecha)
            else
                selectedDate
        )
    }
}

@Composable
fun CategorySelector(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf(
        "Alimentación",
        "Transporte",
        "Salud",
        "Educación",
        "Entretenimiento",
        "Otros",
        "Salario",
        "Alquiler",
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (selectedCategory.isEmpty())
                    "Seleccionar Categoría"
                else
                    selectedCategory
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}