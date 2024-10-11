package com.semonemo.presentation.screen.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.CustomDialog
import com.semonemo.presentation.component.CustomPasswordTextField
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.screen.nft.NftViewModel
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToRegister: (String) -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
    onShowErrorSnackBar: (String) -> Unit,
    navigateToMoment: () -> Unit,
) {
    LoginContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToRegister = navigateToRegister,
        loginViewModel = loginViewModel,
        onShowErrorSnackBar = onShowErrorSnackBar,
        navigateToMoment = navigateToMoment,
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    navigateToRegister: (String) -> Unit,
    nftViewModel: NftViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    onShowErrorSnackBar: (String) -> Unit,
    navigateToMoment: () -> Unit,
) {
    val context = LocalContext.current
    val isInstalled = checkIfMetaMaskInstalled(context)
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (isConnect, setIsConnect) = remember { mutableStateOf(false) }
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    if (showDialog) {
        CustomDialog(
            title = stringResource(R.string.login_dialog_title),
            content =
                stringResource(R.string.login_dialog_content),
            onConfirmMessage = stringResource(R.string.login_dialog_confirm_message),
            onDismissMessage = stringResource(R.string.login_dialog_dismiss_message),
            titleKeywords = listOf(stringResource(R.string.login_title_keywords)),
            contentKeywords = listOf(stringResource(R.string.login_content_keywords)),
            titleBrushFlag = listOf(false),
            contentBrushFlag = listOf(false),
            onDismiss = { setShowDialog(false) },
            onConfirm = {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(BuildConfig.METAMASK_PLAY_STORE_PATH),
                    )
                context.startActivity(intent)
            },
        )
    }

    HandleLoginUiState(
        uiState = uiState,
        onErrorSnackBar = onShowErrorSnackBar,
        uiEvent = loginViewModel.uiEvent,
        navigateToRegister = navigateToRegister,
        onConnectSuccess = {
            setIsConnect(it)
        },
        navigateToMoment = navigateToMoment,
    )

    LoginScreen(
        modifier = modifier,
        onConnect = {
            if (isInstalled.not()) { // 설치 안된 경우
                setShowDialog(true) //
            } else {
                nftViewModel.connect { result ->
                    if (result.contains("Error")) {
                        onShowErrorSnackBar(result)
                    } else {
                        nftViewModel.switchChain(
                            BuildConfig.CHAIN_ID,
                            BuildConfig.CHAIN_NAME,
                            BuildConfig.RPC_URLS,
                            onError = { message, action ->
                                action?.let {
                                    action()
                                } ?: run {
                                    onShowErrorSnackBar(message)
                                }
                            },
                            onSuccess = {
                                loginViewModel.existUser(result)
                            },
                        )
                    }
                }
            }
        },
        onClick = { loginViewModel.login(it) },
        isConnect = isConnect,
    )
}

@Composable
fun HandleLoginUiState(
    uiState: LoginUiState,
    uiEvent: SharedFlow<LoginUiEvent>,
    onErrorSnackBar: (String) -> Unit,
    navigateToRegister: (String) -> Unit,
    onConnectSuccess: (Boolean) -> Unit,
    navigateToMoment: () -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                LoginUiEvent.AutoLogin -> navigateToMoment()
                is LoginUiEvent.RequiredRegister -> {
                    navigateToRegister(event.walletAddress)
                }

                is LoginUiEvent.Error -> onErrorSnackBar(event.errorMessage)
                LoginUiEvent.LoginSuccess -> navigateToMoment()
            }
        }
    }
    LaunchedEffect(uiState) {
        when (uiState) {
            LoginUiState.Init -> {}
            is LoginUiState.Loading -> onConnectSuccess(uiState.isWalletLoading)
        }
    }
}

fun checkIfMetaMaskInstalled(context: Context): Boolean =
    try {
        context.packageManager.getPackageInfo("io.metamask", 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
    onConnect: () -> Unit = {},
    isConnect: Boolean = true,
    onClick: (String) -> Unit = {},
) {
    val offsetY by remember { mutableIntStateOf(0) }
    val (password, setPassword) =
        remember {
            mutableStateOf("")
        }
    LaunchedEffect(isConnect) {
        if (isConnect) {
            delay(700)
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        BoldTextWithKeywords(
            fullText = stringResource(R.string.login_title),
            keywords =
                listOf(
                    stringResource(R.string.brush_moment),
                    stringResource(R.string.brush_frame),
                ),
            brushFlag = listOf(true, true),
            boldStyle = Typography.titleSmall.copy(fontSize = 18.sp),
            normalStyle = Typography.labelLarge.copy(fontSize = 18.sp),
        )
        Spacer(modifier = Modifier.weight(0.01f))
        Text(
            text = stringResource(id = R.string.app_name),
            style = Typography.titleLarge.copy(brush = Main02, fontSize = 50.sp),
        )
        Image(
            painter = painterResource(id = R.drawable.img_start_background),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.weight(0.15f))
        LongWhiteButton(
            modifier =
                Modifier
                    .fillMaxWidth(0.88f),
            icon = R.drawable.img_metamask,
            text = stringResource(R.string.login_message),
            onClick = onConnect,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(
            visible = isConnect,
            enter =
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300),
                ),
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomPasswordTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.88f)
                            .offset { IntOffset(0, offsetY) },
                    focusManager = focusManager,
                    errorMessage = "",
                    input = password,
                    onClearPressed = { setPassword("") },
                    onValueChange = { setPassword(it) },
                    placeholder = stringResource(R.string.input_password_message),
                    containColor = White,
                    borderColor = Gray03,
                    roundDp = 14,
                )

                LongBlackButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.88f),
                    icon = null,
                    text = "로그인하기",
                    onClick = { onClick(password) },
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun LoginScreenPreview() {
    SemonemoTheme {
        LoginScreen()
    }
}
