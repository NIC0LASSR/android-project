package com.example.misfinanzas.screens

import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.misfinanzas.models.MonthlyData
import com.example.misfinanzas.models.TransactionState
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import android.graphics.Color as AndroidColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GraficosScreen(viewModel: TransaccionesViewModel = hiltViewModel()) {
    val ingresosMensuales by viewModel.ingresosMensuales.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()

    when (transactionState) {
        TransactionState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is TransactionState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error al cargar los datos", color = MaterialTheme.colorScheme.error)
            }
        }
        is TransactionState.Success -> {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Gráfico de Ingresos Mensuales",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                LineChartView(ingresosMensuales)
            }
        }

        TransactionState.Idle -> TODO()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineChartView(ingresosMensuales: List<MonthlyData>) {
    if (ingresosMensuales.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No hay datos disponibles para mostrar", color = MaterialTheme.colorScheme.onBackground)
        }
    } else {
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setupLineChart(ingresosMensuales)
                }
            },
            update = { lineChart ->
                lineChart.setupLineChart(ingresosMensuales)
            }
        )
    }
}

private fun LineChart.setupLineChart(ingresosMensuales: List<MonthlyData>) {
    val entries = ingresosMensuales.mapIndexed { index, monthlyData ->
        Entry(index.toFloat(), monthlyData.totalAmount.toFloat())
    }

    val dataSet = LineDataSet(entries, "Ingresos Mensuales").apply {
        color = AndroidColor.BLUE
        valueTextColor = AndroidColor.WHITE
        valueTextSize = 16f
        lineWidth = 2.5f
        setCircleColor(AndroidColor.BLUE)
        setDrawValues(true)
    }

    // Create a list of month labels
    val monthLabels = ingresosMensuales.map { it.month }.toList()

    this.data = LineData(dataSet)
    this.description.text = ""
    this.xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        granularity = 1f
        // Fix: Convert the list to ArrayList<String>
        valueFormatter = IndexAxisValueFormatter(monthLabels as ArrayList<String>)
        setDrawGridLines(false)
    }
    this.axisLeft.axisMinimum = 0f
    this.axisRight.isEnabled = false
    this.animateX(500)
    this.invalidate()
}