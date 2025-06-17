package tech.clique.android.test.ui.screen.detail

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import tech.clique.android.test.data.network.binance.BTCUSDT
import tech.clique.android.test.data.network.binance.INTERVAL_15m
import tech.clique.android.test.data.network.binance.Symbol
import tech.clique.android.test.ui.symbolToDisplayName
import tech.clique.android.test.ui.theme.DecreasingColor
import tech.clique.android.test.ui.theme.IncreasingColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun KLineChartScreen(@Symbol symbol: String) {
    val displayName = symbol.symbolToDisplayName(LocalContext.current)
    val klineViewModel: KlineViewModel = klineViewModel(BTCUSDT, INTERVAL_15m)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(displayName, modifier = Modifier.padding(bottom = 16.dp))

        KLineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            symbol = symbol,
            klineViewModel = klineViewModel,
        )
    }
}

@Composable
fun KLineChart(
    modifier: Modifier,
    symbol: String,
    klineViewModel: KlineViewModel,
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh-mm", Locale.getDefault())
    val candleChart = remember { CandleStickChart(context) }
    val klineDataSet by klineViewModel.items.observeAsState(emptyMap())
    val dataSet = remember {
        CandleDataSet(emptyList(), symbol.symbolToDisplayName(context))
            .apply {
                decreasingColor = DecreasingColor.toArgb()
                increasingColor = IncreasingColor.toArgb()
                decreasingPaintStyle = Paint.Style.FILL
                increasingPaintStyle = Paint.Style.FILL
                shadowColorSameAsCandle = true
                shadowWidth = 1f
                setDrawValues(false)
                barSpace = 0.01f
            }
    }

    LaunchedEffect(Unit) {
        candleChart.let { chart ->
            //notice background conflict with empty dataSet
//            chart.setBackgroundColor(Color.WHITE)
            chart.description.isEnabled = false
            chart.setDrawGridBackground(false)
            chart.setDrawBorders(true)
            chart.setBorderColor(Color.LTGRAY)

            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.setDragOffsetX(10f)
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = false
            chart.setPinchZoom(true)
            chart.isDoubleTapToZoomEnabled = false
            chart.isAutoScaleMinMaxEnabled = true

            chart.marker = KlineMarkerView(context).apply {
                chartView = chart
            }

            chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(true)
                enableGridDashedLine(4f, 4f, 0f)
                gridColor = Color.LTGRAY
                granularity = 1f
                labelCount = 3
                textSize = 5f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        //TODO check invalid
                        val time = klineDataSet[value.toLong()]?.openTime ?: 0
                        return dateFormat.format(Date(time))
                    }
                }
            }

            chart.axisRight.isEnabled = false
            chart.axisLeft.apply {
                setDrawGridLines(true)
                enableGridDashedLine(4f, 4f, 0f)
                gridColor = Color.LTGRAY
                setLabelCount(6, false)
                spaceTop = 0.1f
                spaceBottom = 1f
            }
            chart.legend.isEnabled = false
            chart.data = CandleData(dataSet)
        }
        klineViewModel.updateEvent.collect { (isNew, index, kline) ->
            if (klineDataSet.isEmpty()) return@collect
            val entry = CandleEntry(
                index.toFloat(),
                kline.highPrice.toFloat(),
                kline.lowPrice.toFloat(),
                kline.openPrice.toFloat(),
                kline.closePrice.toFloat(),
                kline
            )
            if (isNew)
                dataSet.addEntry(entry)
            else {
                dataSet.removeEntryByXValue(index.toFloat())
                dataSet.addEntry(entry)
            }
            dataSet.notifyDataSetChanged()
            candleChart.notifyDataSetChanged()
            candleChart.invalidate()
        }

    }


    LaunchedEffect(klineDataSet) {
        if (klineDataSet.isNotEmpty()) {
            val entries = klineDataSet.entries
                .map { (index, kline) ->
                    CandleEntry(
                        index.toFloat(),
                        kline.highPrice.toFloat(),
                        kline.lowPrice.toFloat(),
                        kline.openPrice.toFloat(),
                        kline.closePrice.toFloat(),
                        kline
                    )
                }
            dataSet.values = entries
            dataSet.notifyDataSetChanged()
            candleChart.data.notifyDataChanged()
            candleChart.notifyDataSetChanged()
            candleChart.invalidate()

            val currentRange = candleChart.xChartMax - candleChart.xChartMin
            val targetRange = 30
            val scaleX = currentRange / targetRange
            candleChart.fitScreen()
            candleChart.zoom(scaleX, 1f, candleChart.width / 2f, candleChart.height / 2f, YAxis.AxisDependency.LEFT)
        }
    }

    AndroidView(
        factory = { candleChart },
        modifier = modifier
    )
}