import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.misfinanzas.R
import com.example.misfinanzas.models.FinancialTransaction
import com.example.misfinanzas.models.Transaction_State
import com.example.misfinanzas.screens.TransaccionesViewModel
import com.example.misfinanzas.ui.theme.MisFinanzasTheme

@Composable
fun SaldoScreen(viewModel: TransaccionesViewModel) {
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
    val transactionState by viewModel.transactionState.collectAsState(initial = Transaction_State.Idle)

    val totalBalance = calculateBalance(transactions)
    val totalIncome = calculateIncome(transactions)
    val totalExpense = calculateExpense(transactions)

    MisFinanzasTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header - Saldo Actual
            Text(
                text = stringResource(R.string.saldo_actual_formato),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "$${"%.2f".format(totalBalance)}",
                color = if (totalBalance >= 0) Color.Green else Color.Red,
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Totales de Ingresos y Gastos
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.ingreso),
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "$${"%.2f".format(totalIncome)}",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.gasto),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "$${"%.2f".format(totalExpense)}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Historial de Transacciones
            Text(
                text = stringResource(R.string.historial),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn {
                if (transactionState is Transaction_State.Loading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                } else if (transactions.isNotEmpty()) {
                    items(transactions.size) { index ->
                        TransactionItem(transaction = transactions[index])
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                } else {
                    item {
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
fun TransactionItem(transaction: FinancialTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = transaction.category, fontWeight = FontWeight.Bold)
            Text(
                text = transaction.dateFormatted,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = if (transaction.type == "Ingreso") "+$${transaction.amount}" else "-$${transaction.amount}",
                color = if (transaction.type == "Ingreso") Color.Green else Color.Red
            )
        }
    }
}

// Helper functions to calculate totals
private fun calculateBalance(transactions: List<FinancialTransaction>): Double {
    return transactions.sumOf { if (it.type == "Ingreso") it.amount else -it.amount }
}

private fun calculateIncome(transactions: List<FinancialTransaction>): Double {
    return transactions.filter { it.type == "Ingreso" }.sumOf { it.amount }
}

private fun calculateExpense(transactions: List<FinancialTransaction>): Double {
    return transactions.filter { it.type == "Gasto" }.sumOf { it.amount }
}
