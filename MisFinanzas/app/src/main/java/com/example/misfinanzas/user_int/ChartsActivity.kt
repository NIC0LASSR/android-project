package com.example.misfinanzas.user_int
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misfinanzas.R
import com.example.misfinanzas.viewmodel.TransactionViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ChartsActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficos)

        // Inicializar las vistas
        barChart = findViewById(R.id.barChart)
        lineChart = findViewById(R.id.lineChart)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        // Observar las transacciones y actualizar los gráficos
        viewModel.allTransactions.observe(this, Observer { transactions ->
            // Datos para el gráfico de barras
            val barEntries = mutableListOf<BarEntry>()
            val categoryMap = mutableMapOf<String, Double>()

            // Agrupar por categorías para el gráfico de barras
            transactions.forEach { transaction ->
                val currentAmount = if (categoryMap.containsKey(transaction.category)) {
                    categoryMap[transaction.category]!!
                } else {
                    0.0
                }

                categoryMap[transaction.category] = currentAmount +
                        if (transaction.type == "Ingresos") transaction.amount else -transaction.amount
            }

            var index = 0f
            categoryMap.forEach { (category, amount) ->
                barEntries.add(BarEntry(index++, amount.toFloat()))
            }

            val barDataSet = BarDataSet(barEntries, "Ingresos y Gastos por Categoría")
            val barData = BarData(barDataSet)
            barChart.data = barData
            barChart.invalidate() // Refresca el gráfico de barras

            // Datos para el gráfico de líneas
            val lineEntries = mutableListOf<Entry>()
            val dateMap = mutableMapOf<Long, Double>()

            // Agrupar por fecha para el gráfico de líneas
            transactions.forEach { transaction ->
                val currentAmount = if (dateMap.containsKey(transaction.date)) {
                    dateMap[transaction.date]!!
                } else {
                    0.0
                }

                dateMap[transaction.date] = currentAmount +
                        if (transaction.type == "Ingresos") transaction.amount else -transaction.amount
            }

            dateMap.toSortedMap().forEach { (date, amount) ->
                lineEntries.add(Entry(date.toFloat(), amount.toFloat()))
            }

            val lineDataSet = LineDataSet(lineEntries, "Evolución de Ingresos y Gastos")
            val lineData = LineData(lineDataSet)
            lineChart.data = lineData
            lineChart.invalidate() // Refresca el gráfico de líneas
        })
    }
}
