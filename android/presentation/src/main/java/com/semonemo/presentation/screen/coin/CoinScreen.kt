package com.semonemo.presentation.screen.coin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import com.semonemo.domain.model.WeeklyCoin
import com.semonemo.presentation.R
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.coin.subscreen.CustomCoinChart
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.ProgressGreen
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun CoinRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    viewModel: CoinViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    CoinContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        uiEvent = viewModel.uiEvent,
        uiState = uiState.value,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
fun CoinContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    uiState: CoinUiState,
    uiEvent: SharedFlow<CoinUiEvent>,
    onShowSnackBar: (String) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is CoinUiEvent.Error -> onShowSnackBar(event.errorMessage)
            }
        }
    }

    CoinScreen(
        modifier = modifier,
        coinPrice = uiState.coinPrice.toDouble(),
        coinChanged = uiState.changed,
        weeklyCoin = uiState.weeklyCoin,
        popUpBackStack = popUpBackStack,
        isLoading = uiState.isLoading,
    )

    if (uiState.isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {},
        )
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            loadingMessage = stringResource(R.string.loading_coin_message),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@Composable
fun CoinScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    coinPrice: Double = 100000.0,
    coinChanged: Double = 3.1,
    weeklyCoin: List<WeeklyCoin> = listOf(),
    isLoading: Boolean = true,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(modifier = Modifier, title = {
                Image(
                    painter = painterResource(id = R.drawable.ic_color_sene_coin),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(id = R.string.coin_name))
            }, onNavigationClick = popUpBackStack)
            Column(
                modifier =
                Modifier
                    .wrapContentHeight()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 17.dp, vertical = 30.dp),
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Row(modifier = Modifier, verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = String.format(Locale.KOREAN, "%,.0f", coinPrice),
                        style = Typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = stringResource(id = R.string.coin_unit_name),
                        style = Typography.labelLarge,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier, verticalAlignment = Alignment.Bottom) {
                    val (icon, color) =
                        if (coinChanged > 0) {
                            Pair(R.drawable.ic_arrow_up, Red)
                        } else {
                            Pair(R.drawable.ic_arrow_down, Color.Blue)
                        }

                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = color,
                    )
                    Text(
                        text =
                        "어제보다, " +
                                String.format(
                                    Locale.KOREAN,
                                    "%,.0f ",
                                    coinPrice * coinChanged * 0.01,
                                ),
                        style = Typography.bodySmall.copy(color = color),
                    )

                    Text(
                        text = "  ($coinChanged%)",
                        style = Typography.bodySmall.copy(fontSize = 13.sp, color = color),
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                CustomCoinChart(
                    modifier = Modifier.weight(1f),
                    lowPrice = weeklyCoin.map { it.lowestPrice },
                    highPrice = weeklyCoin.map { it.highestPrice },
                    averagePrice = weeklyCoin.map { it.averagePrice.toLong() },
                    date = weeklyCoin.map { it.date },
                    lineColor = listOf(Color.Red, Color.Blue, ProgressGreen),
                    columnColor = listOf(ProgressGreen),
                )
            }
        }
    }
}

private fun intListAsFloatEntryList(list: List<Int>): List<FloatEntry> {
    val floatEntryList = arrayListOf<FloatEntry>()
    floatEntryList.clear()

    list.forEachIndexed { index, item ->
        floatEntryList.add(entryOf(x = index.toFloat(), y = item.toFloat()))
    }

    return floatEntryList
}

@Composable
@Preview(showBackground = true)
fun CoinScreenPreview() {
    SemonemoTheme {
        CoinScreen()
    }
}
