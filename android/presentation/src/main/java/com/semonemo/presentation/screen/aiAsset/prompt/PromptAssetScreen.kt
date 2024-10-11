package com.semonemo.presentation.screen.aiAsset.prompt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.ScriptTextField
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.aiAsset.draw.AssetButtonList
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PromptRoute(
    modifier: Modifier,
    navigateToDone: (String) -> Unit,
    popUpBackStack: () -> Unit,
    onErrorSnackBar: (String) -> Unit,
    viewModel: PromptViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PromptContent(
        modifier = modifier,
        navigateToDone = navigateToDone,
        popUpBackStack = popUpBackStack,
        onErrorSnackBar = onErrorSnackBar,
        uiEvent = viewModel.uiEvent,
        isLoading = uiState.isLoading,
        makePromptAsset = viewModel::makePromptAsset,
        updateStyle = viewModel::updateStyle,
    )
}

@Composable
fun PromptContent(
    modifier: Modifier,
    navigateToDone: (String) -> Unit,
    popUpBackStack: () -> Unit,
    onErrorSnackBar: (String) -> Unit,
    uiEvent: SharedFlow<PromptUiEvent>,
    isLoading: Boolean,
    makePromptAsset: (String) -> Unit,
    updateStyle: (String, String) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is PromptUiEvent.Error -> onErrorSnackBar(event.errorMessage)
                is PromptUiEvent.NavigateTo -> navigateToDone(event.imageUrl)
            }
        }
    }

    PromptAssetScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        makePromptAsset = makePromptAsset,
        updateStyle = updateStyle,
    )

    if (isLoading) {
        LoadingDialog(
            modifier = Modifier.fillMaxSize(),
            loadingMessage = stringResource(R.string.loading_message),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@Composable
fun PromptAssetScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    makePromptAsset: (String) -> Unit = {},
    updateStyle: (String, String) -> Unit = { _, _ -> },
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    var prompt by remember { mutableStateOf("") }

    val styles =
        listOf(
            "실사",
            "카툰",
            "애니메이션",
        )

    val types =
        listOf(
            "사람",
            "동물",
            "기타",
        )
    var selectedStyle by remember { mutableIntStateOf(0) }
    var selectedType by remember { mutableIntStateOf(0) }

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState),
        color = Color.White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                onNavigationClick = popUpBackStack,
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 22.dp),
            ) {
                BoldTextWithKeywords(
                    modifier = Modifier.padding(top = 15.dp),
                    fullText = stringResource(R.string.prompt_title),
                    keywords = listOf("설명하는 문장", "영어로 작성"),
                    brushFlag = listOf(false, false),
                    boldStyle = Typography.bodyLarge.copy(fontSize = 22.sp),
                    normalStyle = Typography.labelLarge.copy(fontSize = 22.sp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.prompt_example),
                    style = Typography.labelMedium.copy(fontSize = 14.sp),
                    color = Gray02,
                )
                Spacer(modifier = Modifier.height(30.dp))
                ScriptTextField(
                    placeholder = stringResource(R.string.prompt_placeholder),
                    height = 200,
                    value = prompt,
                    onValueChange = { newValue ->
                        prompt = newValue
                    },
                    focusManager = focusManager,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "화풍",
                        style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    AssetButtonList(
                        titles = styles,
                        selectedBtn = styles[selectedStyle],
                        onBtnSelected = {
                            selectedStyle = it
                            updateStyle(styles[selectedStyle], types[selectedType])
                        },
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "종류",
                        style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    AssetButtonList(
                        titles = types,
                        selectedBtn = types[selectedType],
                        onBtnSelected = {
                            selectedType = it
                            updateStyle(styles[selectedStyle], types[selectedType])
                        },
                    )
                }
                Spacer(modifier = Modifier.height(80.dp))
                LongBlackButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = null,
                    text = stringResource(R.string.prompt_done_btn_title),
                    onClick = {
                        makePromptAsset(prompt)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PromptAssetScreenPreview() {
    SemonemoTheme {
//        PromptAssetScreen()
    }
}
