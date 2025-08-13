package com.example.tradeapp.ui.tools.charts

import android.graphics.Color
import android.util.Log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.tradeapp.damin.model.HistoryData
import com.example.tradeapp.viewmodel.ChartViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil
import kotlin.math.floor

// ثابت‌ها
private const val MS_IN_H = 3600_000 // میلی‌ثانیه در یک ساعت
private const val Y_STEP = 100.0 // گام محور Y (قابل تنظیم بر اساس داده‌ها)

// فرمت‌کننده محور X
val BottomAxisValueFormatter = object : CartesianValueFormatter {
    private val dateFormat = SimpleDateFormat("MM/dd h a", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }

    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ) = dateFormat.format(Date((value * MS_IN_H).toLong()))
}
internal val StartAxisValueFormatter = CartesianValueFormatter.decimal(DecimalFormat("$#,###"))
internal val MarkerValueFormatter =
    DefaultCartesianMarker.ValueFormatter.default(DecimalFormat("$#,###.00"))

internal val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ Y_STEP })
@Composable
private fun JetpackComposeGoldPrices(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        rememberCartesianChart(
            rememberCandlestickCartesianLayer(rangeProvider = RangeProvider),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                itemPlacer = StartAxisItemPlacer,
                label = TextComponent().copy(Color.RED)
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                valueFormatter = BottomAxisValueFormatter,
                label = TextComponent().copy(Color.RED)
//                label = TextComponent.Builder().apply {
//                    color = Color.Blue // تغییر رنگ لیبل‌های محور افقی به آبی
//                }.build()
            ),
            marker = rememberMarker(valueFormatter = MarkerValueFormatter, showIndicator = false),
        ),
        modelProducer,
        modifier.height(220.dp),
    )
}
// RangeProvider داینامیک
@Composable
fun rememberRangeProvider(selectedTimeRange: TimeRange): CartesianLayerRangeProvider {
    return object : CartesianLayerRangeProvider {
        override fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore): Double {
            return when (selectedTimeRange) {
                TimeRange.ONE_DAY_BEFORE -> maxX - TimeRange.ONE_DAY.hours
                else -> maxX - selectedTimeRange.hours.coerceAtMost((maxX - minX).toLong())
            }
        }

        override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore): Double {
            return maxX
        }

        override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
            return Y_STEP * floor(minY / Y_STEP)
        }

        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
            return Y_STEP * ceil(maxY / Y_STEP)
        }
    }
}
@Composable
private fun JetpackComposeGoldPrices(
    modelProducer: CartesianChartModelProducer,
    selectedTimeRange: TimeRange,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        rememberCartesianChart(
            rememberCandlestickCartesianLayer(rangeProvider = rememberRangeProvider(selectedTimeRange)),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                itemPlacer = StartAxisItemPlacer,
                label = TextComponent().copy(Color.RED)
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                valueFormatter = BottomAxisValueFormatter,
                label = TextComponent().copy(Color.RED)
            ),
            marker = rememberMarker(valueFormatter = MarkerValueFormatter, showIndicator = false),
        ),
        modelProducer,
        modifier.height(220.dp),
    )
}
// تعریف بازه‌های زمانی
enum class TimeRange(val hours: Long) {
    ONE_DAY(24), // ۱ روز
    ONE_DAY_BEFORE(24), // ۱ روز قبل
    FIVE_DAYS(5 * 24), // ۵ روز
    ONE_MONTH(30 * 24), // ۱ ماه
    FIVE_MONTHS(5 * 30 * 24), // ۵ ماه
    ONE_YEAR(365 * 24), // ۱ سال
    ALL(Long.MAX_VALUE) // نمایش همه داده‌ها
}
// کامپوزابل اصلی با دکمه‌ها
@Composable
fun JetpackComposeGoldPrices(
    modifier: Modifier = Modifier,
    viewModel: ChartViewModel = hiltViewModel(),
) {
    var selectedTimeRange by remember { mutableStateOf(TimeRange.ALL) }
    val modelProducer = remember { CartesianChartModelProducer() }

    // لود داده‌ها
    LaunchedEffect(Unit) {
        viewModel.fetchChartData().collectLatest { data ->
            if (data.s == "ok") {
                modelProducer.runTransaction {
                    candlestickSeries(
                        x = data.t.map { it.toDouble() },
                        opening = data.o,
                        closing = data.c,
                        low = data.l,
                        high = data.h
                    )
                }
            }
            delay(6000) // به‌روزرسانی هر ۶۰ ثانیه
        }
    }

    // رابط کاربری
    Column(modifier = modifier) {
        // نمودار
        JetpackComposeGoldPrices(modelProducer, selectedTimeRange, Modifier.fillMaxWidth())
    }
}
val RangeProvider = object : CartesianLayerRangeProvider {
    override fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore): Double {
        // استفاده از حداقل مقدار X از داده‌ها
        return minX
    }

    override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore): Double {
        // استفاده از حداکثر مقدار X از داده‌ها
        return maxX
    }

    override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
        // نگه داشتن تنظیمات قبلی برای محور Y
        return Y_STEP * floor(minY / Y_STEP)
    }

    override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
        // نگه داشتن تنظیمات قبلی برای محور Y
        return Y_STEP * ceil(maxY / Y_STEP)
    }
}














//
//// ثابت‌ها
//private const val MS_IN_H = 3600_000L // میلی‌ثانیه در یک ساعت
//private const val Y_STEP = 100.0 // گام محور Y
//
//// فرمت‌کننده محور X
//val BottomAxisValueFormatter = object : CartesianValueFormatter {
//    private val dateFormat = SimpleDateFormat("MM/dd h a", Locale.US).apply {
//        timeZone = TimeZone.getTimeZone("GMT")
//    }
//
//    override fun format(
//        context: CartesianMeasuringContext,
//        value: Double,
//        verticalAxisPosition: Axis.Position.Vertical?,
//    ) = dateFormat.format(Date((value * MS_IN_H).toLong()))
//}
//
//// تعریف بازه‌های زمانی
//enum class TimeRange(val hours: Long) {
//    ONE_DAY(24), // ۱ روز
//    ONE_DAY_BEFORE(24), // ۱ روز قبل
//    FIVE_DAYS(5 * 24), // ۵ روز
//    ONE_MONTH(30 * 24), // ۱ ماه
//    FIVE_MONTHS(5 * 30 * 24), // ۵ ماه
//    ONE_YEAR(365 * 24), // ۱ سال
//    ALL(Long.MAX_VALUE) // نمایش همه داده‌ها
//}
//
//// فرض بر این است که ChartViewModel و مدل داده‌ها به این صورت تعریف شده‌اند
//data class ChartData(
//    val s: String,
//    val t: List<Long>, // timestampها به میلی‌ثانیه
//    val o: List<Number>,
//    val c: List<Number>,
//    val l: List<Number>,
//    val h: List<Number>
//)
//
//// RangeProvider داینامیک
//@Composable
//fun rememberRangeProvider(selectedTimeRange: TimeRange): CartesianLayerRangeProvider {
//    return remember(selectedTimeRange) {
//        object : CartesianLayerRangeProvider {
//            override fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore): Double {
//                val result = when (selectedTimeRange) {
//                    TimeRange.ONE_DAY_BEFORE -> maxX - 48 // از ۴۸ تا ۲۴ ساعت قبل
//                    else -> maxX - selectedTimeRange.hours.coerceAtMost((maxX - minX).toLong())
//                }
//                println("minX: $minX, maxX: $maxX, selectedMinX: $result")
//                return result
//            }
//
//            override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore): Double {
//                println("maxX: $maxX")
//                return maxX
//            }
//
//            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
//                return Y_STEP * floor(minY / Y_STEP)
//            }
//
//            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
//                return Y_STEP * ceil(maxY / Y_STEP)
//            }
//        }
//    }
//}
//
//// کامپوزابل نمودار
//@Composable
//fun JetpackComposeGoldPrices(
//    modelProducer: CartesianChartModelProducer,
//    selectedTimeRange: TimeRange,
//    modifier: Modifier = Modifier,
//) {
//    CartesianChartHost(
//        chart = rememberCartesianChart(
//            layers = arrayOf(
//                rememberCandlestickCartesianLayer(rangeProvider = rememberRangeProvider(selectedTimeRange))
//            ),
//            startAxis = VerticalAxis.rememberStart(
//                valueFormatter = StartAxisValueFormatter,
//                itemPlacer = StartAxisItemPlacer,
//                label = TextComponent().copy(color = Color.RED)
//            ),
//            bottomAxis = HorizontalAxis.rememberBottom(
//                guideline = null,
//                valueFormatter = BottomAxisValueFormatter,
//                label = TextComponent().copy(color = Color.RED)
//            ),
//            marker = rememberMarker(valueFormatter = MarkerValueFormatter, showIndicator = false),
//        ),
//        modelProducer = modelProducer,
//        modifier = modifier.height(220.dp),
//    )
//}
//
//// کامپوزابل اصلی با دکمه‌ها
//@Composable
//fun GoldPricesChart(
//    modifier: Modifier = Modifier,
//    viewModel: ChartViewModel = hiltViewModel(),
//) {
//    var selectedTimeRange = remember { mutableStateOf(TimeRange.ALL) }
//    val modelProducer = remember { CartesianChartModelProducer() }
//
//    // لود داده‌ها
//    LaunchedEffect(selectedTimeRange.value) {
//        viewModel.fetchChartData().collectLatest { data ->
//            if (data.s == "ok") {
//                println("Loaded data size: ${selectedTimeRange.value}")
//                val currentTime = System.currentTimeMillis() / MS_IN_H
//                val minTime = when (selectedTimeRange.value) {
//                    TimeRange.ONE_DAY_BEFORE -> currentTime - 48 // از ۴۸ تا ۲۴ ساعت قبل
//                    TimeRange.ONE_DAY ->currentTime - 24 // آخرین ۲۴ ساعت
//                    TimeRange.FIVE_DAYS -> currentTime - (5 * 24) // ۵ روز گذشته
//                    TimeRange.ONE_MONTH -> currentTime - (30 * 24) // ۱ ماه گذشته
//                    TimeRange.FIVE_MONTHS -> currentTime - (5 * 30 * 24) // ۵ ماه گذشته
//                    TimeRange.ONE_YEAR -> currentTime - (365 * 24) // ۱ سال گذشته
//                    TimeRange.ALL -> Long.MIN_VALUE // تمام داده‌ها
//                }
//
//                val filteredData = data.t.mapIndexedNotNull { index, time ->
//                    val timeInHours = time / MS_IN_H
//                    if (timeInHours >= minTime && timeInHours <= currentTime) {
//                        index
//                    } else {
//                        null
//                    }
//                }
//                println("Filtered data size: ${filteredData.size}")
//                if (filteredData.isNotEmpty()) {
//                    modelProducer.runTransaction {
//                        candlestickSeries(
//                            x = filteredData.map { (data.t[it] ).toDouble() },
//                            opening = filteredData.map { data.o[it] },
//                            closing = filteredData.map { data.c[it] },
//                            low = filteredData.map { data.l[it] },
//                            high = filteredData.map { data.h[it] }
//                        )
//                    }
//                } else {
//                    println("No data available for the selected time range: $selectedTimeRange.value")
//                }
//            } else {
//                println("Failed to load data: ${data.s}")
//            }
//        }
//    }
//
//    // رابط کاربری
//    Column(modifier = modifier) {
//        // نمودار
//        JetpackComposeGoldPrices(modelProducer, selectedTimeRange.value, Modifier.fillMaxWidth())
//
//        // دکمه‌ها برای تغییر بازه زمانی
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(onClick = { selectedTimeRange.value = TimeRange.ONE_DAY_BEFORE }) {
//                Text("۱ روز قبل")
//            }
//            Button(onClick = { selectedTimeRange.value = TimeRange.ONE_DAY }) {
//                Text("۱ روز")
//            }
//            Button(onClick = { selectedTimeRange.value = TimeRange.FIVE_DAYS }) {
//                Text("۵ روز")
//            }
//            Button(onClick = { selectedTimeRange.value = TimeRange.ONE_MONTH }) {
//                Text("۱ ماه")
//            }
//            Button(onClick = { selectedTimeRange.value = TimeRange.FIVE_MONTHS }) {
//                Text("۵ ماه")
//            }
//            Button(onClick = { selectedTimeRange.value = TimeRange.ONE_YEAR }) {
//                Text("۱ سال")
//            }
//            Button(onClick = { selectedTimeRange.value = TimeRange.ALL }) {
//                Text("همه")
//            }
//        }
//    }
//}