import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import com.example.misfinanzas.screens.TransaccionesViewModel
import androidx.compose.ui.graphics.Color as ComposeColor
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun GraficosScreen(viewModel: TransaccionesViewModel) {
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gráficos de Transacciones",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra gráfico de barras
        Text(
            text = "Gastos Mensuales",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BarChartView()

        Spacer(modifier = Modifier.height(32.dp))

        // Muestra gráfico de línea
        Text(
            text = "Tendencia de Ingresos",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LineChartView()
    }
}

@Composable
fun BarChartView() {
    AndroidView(factory = { context ->
        BarChart(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                600
            )

            // Datos de ejemplo para el gráfico de barras
            val entries = listOf(
                BarEntry(1f, 500f),
                BarEntry(2f, 600f),
                BarEntry(3f, 800f),
                BarEntry(4f, 300f),
                BarEntry(5f, 400f)
            )
            val dataSet = BarDataSet(entries, "Gastos")
            dataSet.color = AndroidColor.RED
            data = BarData(dataSet)

            // Configuración del eje X
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)

            // Configuración del eje Y
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false

            // Configuración adicional
            description.isEnabled = false
            legend.isEnabled = true
            animateY(1000)
        }
    })
}

@Composable
fun LineChartView() {
    AndroidView(factory = { context ->
        LineChart(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                600
            )

            // Datos de ejemplo para el gráfico de líneas
            val entries = listOf(
                Entry(1f, 1000f),
                Entry(2f, 1100f),
                Entry(3f, 1200f),
                Entry(4f, 900f),
                Entry(5f, 950f)
            )
            val dataSet = LineDataSet(entries, "Ingresos")
            dataSet.color = AndroidColor.BLUE
            dataSet.setCircleColor(AndroidColor.BLUE)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 4f
            data = LineData(dataSet)

            // Configuración del eje X
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)

            // Configuración del eje Y
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false

            // Configuración adicional
            description.isEnabled = false
            legend.isEnabled = true
            animateY(1000)
        }
    })
}
