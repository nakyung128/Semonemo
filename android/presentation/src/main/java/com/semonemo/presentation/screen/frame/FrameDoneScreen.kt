package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.HashTagTextField
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.ScriptTextField
import com.semonemo.presentation.screen.nft.NftEvent
import com.semonemo.presentation.screen.nft.NftViewModel
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.converterFile
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import java.io.File

@Composable
fun FrameDoneRoute(
    modifier: Modifier = Modifier,
    viewModel: FrameViewModel = hiltViewModel(),
    nftViewModel: NftViewModel = hiltViewModel(),
    navigateToMoment: () -> Unit = {},
    onErrorSnackBar: (String) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    FrameDoneContent(
        modifier = modifier,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        navigateToMoment = navigateToMoment,
        onClick = viewModel::uploadImage,
        updateContent = viewModel::updateContent,
        updateTitle = viewModel::updateTitle,
        addTags = viewModel::addTag,
        deleteTags = viewModel::deleteTag,
        sendTransaction = { data ->
            nftViewModel.sendTransaction(
                function =
                    Function(
                        "mintNFT",
                        listOf(Utf8String(data)),
                        listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}),
                    ),
                onSuccess = {
                    viewModel.publishNft(imageHash = it, ipfsHash = data)
                },
                onError = {
                    onErrorSnackBar(it)
                },
                contractAddress = BuildConfig.NFT_CONTRACT_ADDRESS,
            )
        },
        nftEvent = nftViewModel.nftEvent,
    )
}

@Composable
fun FrameDoneContent(
    modifier: Modifier,
    uiState: FrameUiState,
    navigateToMoment: () -> Unit = {},
    onClick: (File) -> Unit = {},
    updateTitle: (String) -> Unit = {},
    updateContent: (String) -> Unit = {},
    uiEvent: SharedFlow<FrameUiEvent>,
    nftEvent: SharedFlow<NftEvent>,
    onErrorSnackBar: (String) -> Unit = {},
    addTags: (String) -> Unit = {},
    deleteTags: (String) -> Unit = {},
    sendTransaction: (String) -> Unit = {},
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is FrameUiEvent.Error -> onErrorSnackBar(event.errorMessage)
                is FrameUiEvent.UploadFinish -> sendTransaction(event.imageHash)
                FrameUiEvent.NavigateMoment -> {
                    onErrorSnackBar("프레임 제작에 성공했습니다.")
                    navigateToMoment()
                }
            }
        }
    }

    LaunchedEffect(nftEvent) {
        nftEvent.collectLatest { event ->
            when (event) {
                is NftEvent.Error -> onErrorSnackBar(event.errorMessage)
                is NftEvent.SendTransaction -> {}
            }
        }
    }
    FrameDoneScreen(
        modifier = modifier,
        navigateToMoment = navigateToMoment,
        onClick = onClick,
        bitmap = uiState.bitmap,
        updateContent = updateContent,
        updateTitle = updateTitle,
        tags = uiState.tags,
        addTags = addTags,
        deleteTags = deleteTags,
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
            loadingMessage = stringResource(R.string.frame_loading_title),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@Composable
fun FrameDoneScreen(
    modifier: Modifier = Modifier,
    bitmap: Bitmap? = null,
    navigateToMoment: () -> Unit = {},
    onClick: (File) -> Unit = {},
    updateTitle: (String) -> Unit = {},
    updateContent: (String) -> Unit = {},
    tags: List<String> = listOf(),
    addTags: (String) -> Unit = {},
    deleteTags: (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf("") }
    var script by remember { mutableStateOf("") }
    val context = LocalContext.current
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = Color.White)
                .verticalScroll(state = scrollState),
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.frame_done_title),
                    style = Typography.titleLarge.copy(fontSize = 30.sp, brush = Main02),
                )
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter = painterResource(id = R.drawable.img_clapping_hands),
                    contentDescription = "clapping_hands",
                    tint = Color.Unspecified,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text =
                    stringResource(R.string.frame_done_script),
                textAlign = TextAlign.Center,
                style = Typography.labelMedium.copy(fontSize = 15.sp),
                color = Gray01,
            )
            Spacer(modifier = Modifier.height(30.dp))
            bitmap?.let {
                Image(
                    modifier = Modifier.wrapContentSize(),
                    bitmap = it.asImageBitmap(),
                    contentDescription = "frame",
                )
            }
            Spacer(modifier = Modifier.height(35.dp))
            HashTagTextField(
                modifier = Modifier.fillMaxWidth(0.88f),
                onTagAddAction = { keyword ->
                    if (keyword.isNotBlank()) {
                        addTags(keyword)
                    }
                },
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(0.88f),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                content = {
                    items(count = tags.size) { index ->
                        HashTag(
                            keyword = tags[index],
                            isEdit = true,
                            onCloseClicked = { deleteTags(tags[index]) },
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                modifier =
                    Modifier
                        .fillMaxWidth(0.88f)
                        .height(48.dp),
                placeholder = stringResource(R.string.frame_title_placeholder),
                input = title,
                onValueChange = {
                    title = it
                    updateTitle(it)
                },
                onClearPressed = { title = "" },
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ScriptTextField(
                modifier = Modifier.fillMaxWidth(0.88f),
                placeholder = stringResource(R.string.frame_script_placeholder),
                height = 130,
                value = script,
                onValueChange = {
                    script = it
                    updateContent(it)
                },
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.height(40.dp))
            LongBlackButton(
                icon = null,
                text = stringResource(R.string.frame_save_btn_title),
                onClick = {
                    bitmap?.let {
                        onClick(converterFile(it))
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.frame_save_btn_script),
                style = Typography.bodySmall.copy(fontSize = 13.sp),
                color = Gray02,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FrameDoneScreenPreview() {
    SemonemoTheme {
        FrameDoneScreen(
            tags = listOf("아이유", "이재한"),
        )
    }
}
