import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.misfinanzas.R
import com.example.misfinanzas.models.FinancialTransaction
import com.example.misfinanzas.models.Transaction_State
import com.example.misfinanzas.screens.TransaccionesViewModel
import com.example.misfinanzas.ui.theme.MisFinanzasTheme
import kotlinx.coroutines.launch

@Composable
fun HistorialScreen(viewModel: TransaccionesViewModel = viewModel()) {
    val context = LocalContext.current
    val transactionState by viewModel.transactionState.collectAsState(Transaction_State.Idle)
    val balance by viewModel.balance.collectAsState(0.0)
    val transactions by viewModel.transactions.collectAsState(emptyList())

    MisFinanzasTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Color(0xFFF5F5F5))
        ) {
            // Header - Balance actual
            Text(
                text = stringResource(R.string.historial_transacciones, "%.2f".format(balance)),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp),
                color = if (balance >= 0) Color.Green else Color.Red
            )

            // Estado de carga y error
            when (transactionState) {
                is Transaction_State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
                is Transaction_State.Error -> {
                    Text(
                        text = (transactionState as Transaction_State.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    // Contenido principal
                    if (transactions.isNotEmpty()) {
                        TransactionList(transactions) { transaction ->
                            viewModel.deleteTransaction(transaction)
                            Toast.makeText(context, "Transacción eliminada", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.no_transacciones),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionList(
    transactions: List<FinancialTransaction>,
    onDeleteTransaction: (FinancialTransaction) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items(transactions.size) { index ->
            TransactionItem(transaction = transactions[index], onDeleteTransaction = onDeleteTransaction)
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun TransactionItem(
    transaction: FinancialTransaction,
    onDeleteTransaction: (FinancialTransaction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = transaction.category,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = transaction.dateFormatted,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = if (transaction.type == "Ingreso") "+$${transaction.amount}" else "-$${transaction.amount}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (transaction.type == "Ingreso") Color.Green else Color.Red
            )
        }
        IconButton(onClick = { onDeleteTransaction(transaction) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.eliminar_transaccion),
                tint = Color.Red
            )
        }
    }
}
