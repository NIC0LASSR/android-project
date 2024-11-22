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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.format.TextStyle
import java.util.Locale
import android.graphics.Color as AndroidColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GraficosScreen(viewModel: TransaccionesViewModel = hiltViewModel()) {
    val gastosMensuales by viewModel.gastosMensuales.collectAsState()
    val ingresosMensuales by viewModel.ingresosMensuales.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()

    Log.d("GraficosScreen", "Transaction state: $transactionState")

    when (transactionState) {
        is TransactionState.Loading -> {
            Log.d("GraficosScreen", "Loading data")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is TransactionState.Error -> {
            Log.e("GraficosScreen", "Error: ${(transactionState as TransactionState.Error).message}")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (transactionState as TransactionState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is TransactionState.Success, is TransactionState.Idle -> {
            Log.d("GraficosScreen", "Displaying data")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Gráficos de Transacciones",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Gastos Mensuales",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BarChartView(gastosMensuales)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Tendencia de Ingresos",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LineChartView(ingresosMensuales)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BarChartView(gastosMensuales: List<MonthlyData>) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setupBarChart(gastosMensuales)
            }
        },
        update = { barChart ->
            barChart.setupBarChart(gastosMensuales)
        }
    )
}

private fun BarChart.setupBarChart(gastosMensuales: List<MonthlyData>) {
    val entries = gastosMensuales.mapIndexed { index, monthlyData ->
        BarEntry(index.toFloat(), monthlyData.totalAmount.toFloat())
    }

    val dataSet = BarDataSet(entries, "Gastos Mensuales").apply {
        color = AndroidColor.RED
        valueTextSize = 16f
        valueTextColor = AndroidColor.WHITE
    }

    this.data = BarData(dataSet)
    this.description.text = ""
    this.xAxis.position = XAxis.XAxisPosition.BOTTOM
    this.axisLeft.axisMinimum = 0f
    this.axisRight.isEnabled = false
    this.setFitBars(true)
    this.animateY(500)
    this.invalidate()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineChartView(ingresosMensuales: List<MonthlyData>) {
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

    this.data = LineData(dataSet)
    this.description.text = ""
    this.xAxis.position = XAxis.XAxisPosition.BOTTOM
    this.axisLeft.axisMinimum = 0f
    this.axisRight.isEnabled = false
    this.animateX(500)
    this.invalidate()
}