package com.semonemo.presentation.screen.signup

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.CustomPasswordTextField
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.Validator
import com.semonemo.presentation.util.addFocusCleaner
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.toAbsolutePath
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@Composable
fun SignUpRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowErrorSnackBar: (String) -> Unit = {},
) {
    SignUpContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowErrorSnackBar = onShowErrorSnackBar,
    )
}

@Composable
fun SignUpContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    onShowErrorSnackBar: (String) -> Unit,
) {
    val uiState by signUpViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(signUpViewModel.uiEvent) {
        signUpViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is SignUpUiEvent.Error -> onShowErrorSnackBar(event.message)
                SignUpUiEvent.SignUpSuccess -> popUpBackStack()
            }
        }
    }

    SignUpScreen(
        modifier = modifier,
        nickname = uiState.nickname,
        password = uiState.password,
        updateNickname = { signUpViewModel.updateNickname(it) },
        updatePassword = { signUpViewModel.updatePassword(it) },
        isFinished = uiState.validate(),
        onClick = signUpViewModel::signUp,
        createImage = { uri ->
            val image = File(uri.toAbsolutePath(context))
            signUpViewModel.updateProfileImage(image)
        },
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
            loadingMessage = stringResource(R.string.signup_loading_message),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    nickname: String = "",
    password: String = "",
    updateNickname: (String) -> Unit = {},
    updatePassword: (String) -> Unit = {},
    isFinished: Boolean = false,
    onClick: () -> Unit = {},
    createImage: (Uri) -> Unit = {},
) {
    val (profile, setProfile) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    createImage(it)
                    setProfile(it.toString())
                }
            },
        )

    Column(
        modifier =
            modifier
                .background(brush = Main01)
                .padding(top = 50.dp)
                .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoldTextWithKeywords(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Start),
            fullText = stringResource(R.string.register_title),
            keywords =
                listOf(
                    stringResource(R.string.brush_need),
                    stringResource(R.string.brush_persenal_info),
                ),
            brushFlag = listOf(false, false),
            boldStyle = Typography.titleSmall.copy(fontSize = 25.sp),
            normalStyle = Typography.labelLarge.copy(fontSize = 25.sp),
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        if (profile.isNotBlank()) {
            GlideImage(
                imageModel = profile.toUri(),
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(140.dp)
                        .clip(shape = CircleShape)
                        .noRippleClickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                                ),
                            )
                        },
                loading = {
                    ImageLoadingProgress(
                        modifier = Modifier,
                    )
                },
                failure = {
                    Image(
                        painter =
                            painterResource(
                                id =
                                    R.drawable.ic_place_holder,
                            ),
                        contentDescription = null,
                    )
                },
            )
        } else {
            Image(
                modifier =
                    Modifier
                        .size(140.dp)
                        .noRippleClickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                                ),
                            )
                        },
                painter = painterResource(id = R.drawable.img_user_profile_add),
                contentScale = ContentScale.Crop,
                contentDescription = "",
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        CustomTextField(
            modifier = Modifier.fillMaxWidth(0.88f),
            focusManager = focusManager,
            errorMessage = Validator.validationNickname(nickname),
            input = nickname,
            onClearPressed = { updateNickname("") },
            onValueChange = { updateNickname(it) },
            placeholder = stringResource(R.string.input_nickname_message),
        )
        CustomPasswordTextField(
            modifier = Modifier.fillMaxWidth(0.88f),
            focusManager = focusManager,
            errorMessage = Validator.validationPassword(password),
            input = password,
            onClearPressed = { updatePassword("") },
            onValueChange = { updatePassword(it) },
            placeholder = stringResource(R.string.input_password_message),
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isFinished) {
            LongBlackButton(
                icon = null,
                text = stringResource(R.string.register_message),
                onClick = onClick,
            )
        } else {
            LongUnableButton(
                modifier = Modifier.fillMaxWidth(0.88f),
                text = stringResource(id = R.string.register_message),
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
@Preview(showSystemUi = true)
fun RegisterScreenPreview() {
    SemonemoTheme {
        SignUpScreen(modifier = Modifier.fillMaxSize())
    }
}
