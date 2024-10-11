package com.semonemo.presentation.screen.coin.subscreen

import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entryOf
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.ProgressGreen
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.SemonemoTheme

@Composable
fun CustomCoinChart(
    modifier: Modifier = Modifier,
    maxPrice: Long = 10L,
    minPrice: Long = 0L,
    lineColor: List<Color>,
    columnColor: List<Color>,
    lowPrice: List<Long>,
    highPrice: List<Long>,
    averagePrice: List<Long>,
    date: List<String>,
) {
    val lowPriceProducer = ChartEntryModelProducer(intListAsFloatEntryList(lowPrice))
    val highPriceProducer =
        ChartEntryModelProducer(intListAsFloatEntryList(highPrice))
    val averagePriceProducer =
        ChartEntryModelProducer(intListAsFloatEntryList(averagePrice))
    ProvideChartStyle(
        rememberChartStyle(
            lineChartColors = lineColor,
            columnChartColors = columnColor,
        ),
    ) {
        val highPriceChart =
            LineChart(
                lines =
                    listOf(
                        lineSpec(
                            lineColor = Color.Red,
                            lineThickness = 1.dp,
                            dataLabel =
                                TextComponent
                                    .Builder()
                                    .apply {
                                        color = Color.Red.toArgb()
                                        textSizeSp = 14f
                                        typeface = Typeface.DEFAULT_BOLD
                                    }.build(),
                            lineBackgroundShader = null,
                        ),
                    ),
            )

        val lowPriceChart =
            LineChart(
                lines =
                    listOf(
                        lineSpec(
                            lineColor = Color.Blue,
                            lineThickness = 1.dp,
                            dataLabel =
                                TextComponent
                                    .Builder()
                                    .apply {
                                        color = Color.Blue.toArgb()
                                        textSizeSp = 14f
                                        typeface = Typeface.DEFAULT_BOLD
                                    }.build(),
                            lineBackgroundShader = null,
                        ),
                    ),
            )

        val averagePriceChart =
            columnChart(
                mergeMode = ColumnChart.MergeMode.Stack,
                axisValuesOverrider =
                    AxisValuesOverrider.fixed(
                        minY = minPrice.toFloat(),
                        maxY = maxPrice.toFloat(),
                    ),
                spacing = 100.dp,
                dataLabel =
                    TextComponent
                        .Builder()
                        .apply {
                            color = ProgressGreen.toArgb()
                            textSizeSp = 14f
                            typeface = Typeface.DEFAULT_BOLD
                        }.build(),
            )
        val chartModelProducer =
            ComposedChartEntryModelProducer(
                averagePriceProducer,
                highPriceProducer,
                lowPriceProducer,
            )

        Chart(
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(400.dp),
            chart =
                remember(averagePriceChart + highPriceChart + lowPriceChart) {
                    averagePriceChart + highPriceChart + lowPriceChart
                },
            legend = rememberLegend(colors = lineColor),
            chartModelProducer = chartModelProducer,
            startAxis =
                rememberStartAxis(
                    itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = date.size),
                ),
            bottomAxis =
                rememberBottomAxis(
                    valueFormatter = { value, _ ->
                        date.getOrNull(value.toInt()) ?: ""
                    },
                ),
            runInitialAnimation = true,
            chartScrollState = rememberChartScrollState(),
        )
    }
}

private fun intListAsFloatEntryList(list: List<Long>): List<FloatEntry> {
    val floatEntryList = arrayListOf<FloatEntry>()
    floatEntryList.clear()

    list.forEachIndexed { index, item ->
        floatEntryList.add(entryOf(x = index.toFloat(), y = item.toFloat()))
    }

    return floatEntryList
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CoinChartPreview() {
    SemonemoTheme {
        CustomCoinChart(
            modifier = Modifier.fillMaxSize(),
            lineColor = listOf(Color.Red, Color.Blue, GunMetal),
            columnColor = listOf(GunMetal),
            lowPrice = listOf(0L, 1L, 2L, 3L, 4L, 5L, 1L, 1L, 1L, 1L),
            highPrice = listOf(0L, 3L, 2L, 3L, 4L, 5L, 1L, 1L, 1L, 1L),
            averagePrice = listOf(0L, 3L, 2L, 3L, 4L, 5L, 1L, 1L, 1L, 1L),
            date =
                listOf(
                    "10-01",
                    "10-02",
                    "10-03",
                    "10-04",
                    "10-05",
                    "10-06",
                    "10-07",
                    "10-08",
                    "10-09",
                    "10-10",
                ),
        )
    }
}
