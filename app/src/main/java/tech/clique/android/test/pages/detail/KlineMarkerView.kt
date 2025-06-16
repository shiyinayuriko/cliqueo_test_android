package tech.clique.android.test.pages.detail

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import tech.clique.android.test.R
import tech.clique.android.test.data.KlineData
import tech.clique.android.test.databinding.KlineMarkerViewBinding
import tech.clique.android.test.pages.toTrimmedPrice
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KlineMarkerView(context: Context) : MarkerView(context, R.layout.kline_marker_view) {
    private val binding: KlineMarkerViewBinding = KlineMarkerViewBinding.bind(findViewById(R.id.kline_marker_view_content))
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh-mm", Locale.getDefault())

    override fun refreshContent(e: Entry, highlight: Highlight) {
        ((e as? CandleEntry)?.data as? KlineData)?.let { klineData ->
            val date = Date(klineData.openTime)
            binding.tvDate.text = context.getString(R.string.kline_marker_text_open_time, dateFormat.format(date))
            binding.tvOpen.text = context.getString(R.string.kline_marker_text_open_price, klineData.openPrice.toTrimmedPrice())
            binding.tvHigh.text = context.getString(R.string.kline_marker_text_high_price, klineData.highPrice.toTrimmedPrice())
            binding.tvLow.text = context.getString(R.string.kline_marker_text_low_price, klineData.lowPrice.toTrimmedPrice())
            binding.tvClose.text = context.getString(R.string.kline_marker_text_close_price, klineData.closePrice.toTrimmedPrice())
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}