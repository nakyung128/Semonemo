package com.semonemo.presentation.screen.coin.subscreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.horizontalLegend
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.legend.HorizontalLegend
import com.patrykandpatrick.vico.views.chart.line.lineSpec

@Composable
fun rememberChartStyle(
    lineChartColors: List<Color>,
    columnChartColors: List<Color>,
): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(lineChartColors, columnChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light

        ChartStyle(
            axis =
                ChartStyle.Axis(
                    axisLabelColor = Color(defaultColors.axisLabelColor),
                    axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
                    axisLineColor = Color(defaultColors.axisLineColor),
                ),
            lineChart =
                ChartStyle.LineChart(
                    lines =
                        lineChartColors.mapIndexed { index, color ->
                            lineSpec(
                                lineColor = color,
                                lineThickness = 1.dp, // 필요한 두께로 설정
                                dataLabel = TextComponent.Builder().build(), // 데이터 라벨을 추가
                                lineBackgroundShader = null
                            )
                        },
                ),
            columnChart =
                ChartStyle.ColumnChart(
                    columns =
                        columnChartColors.map { columnColor ->
                            LineComponent(
                                color = columnColor.toArgb(),
                                thicknessDp = 30f, // 필요한 두께로 설정
                                shape = Shapes.cutCornerShape(topRightPercent = 10, topLeftPercent = 10),
                            )
                        },
                ),
            marker = ChartStyle.Marker(),
            elevationOverlayColor = Color(defaultColors.elevationOverlayColor),
        )
    }
}

@Composable
fun rememberLegend(colors: List<Color>): HorizontalLegend {
    val labelTextList = listOf("최고가", "최저가", "평균가")

    return horizontalLegend(
        items =
            List(labelTextList.size) { index ->
                legendItem(
                    icon =
                        shapeComponent(
                            shape = Shapes.pillShape,
                            color = colors[index],
                        ),
                    label = textComponent(),
                    labelText = labelTextList[index],
                )
            },
        iconSize = 10.dp,
        iconPadding = 8.dp,
        spacing = 10.dp,
        padding = dimensionsOf(top = 8.dp),
    )
}
