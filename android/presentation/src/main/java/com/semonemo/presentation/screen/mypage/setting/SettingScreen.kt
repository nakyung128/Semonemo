package com.semonemo.presentation.screen.mypage.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.Validator
import com.semonemo.presentation.util.noRippleClickable
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    SettingContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToLogin = navigateToLogin,
        uiEvent = viewModel.uiEvent,
        onShowSnackBar = onShowSnackBar,
        logout = viewModel::logout,
        deleteUser = viewModel::deleteUser,
        validateNickname = viewModel::validateNickname,
    )
}

@Composable
fun SettingContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    navigateToLogin: () -> Unit,
    uiEvent: SharedFlow<SettingUiEvent>,
    onShowSnackBar: (String) -> Unit,
    logout: () -> Unit,
    deleteUser: () -> Unit,
    validateNickname: (String) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is SettingUiEvent.Error -> onShowSnackBar(event.message)
                is SettingUiEvent.EditSuccess -> onShowSnackBar(event.message)
                SettingUiEvent.Logout -> {
                    navigateToLogin()
                }
            }
        }
    }

    SettingScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        logout = logout,
        deleteUser = deleteUser,
        editNickname = validateNickname,
    )
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    logout: () -> Unit = {},
    deleteUser: () -> Unit = {},
    editNickname: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var nickname by remember { mutableStateOf("") }
    var errorMessage = Validator.validationNickname(nickname)
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = Color.White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.setting_title),
                        style = Typography.bodySmall,
                    )
                },
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = stringResource(R.string.edit_title),
                    style = Typography.bodySmall.copy(fontSize = 18.sp),
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            CustomTextField(
                modifier = Modifier.padding(horizontal = 10.dp),
                placeholder = stringResource(R.string.edit_place_holder),
                input = nickname,
                onValueChange = { nickname = it },
                errorMessage = errorMessage,
                focusManager = focusManager,
                onClearPressed = { nickname = "" },
            )
            if (nickname.isNotEmpty() && errorMessage.isBlank()) {
                LongBlackButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                    icon = null,
                    text = stringResource(R.string.edit_button_message),
                    onClick = { editNickname(nickname) },
                )
            } else {
                LongUnableButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                    text = stringResource(id = R.string.edit_button_message),
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .noRippleClickable {
                            logout()
                        },
            ) {
                Text(
                    text = stringResource(R.string.logout_message),
                    style = Typography.bodySmall.copy(fontSize = 18.sp),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_next),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .noRippleClickable {
                            deleteUser()
                        },
            ) {
                Text(
                    text = stringResource(R.string.delete_user_message),
                    style = Typography.bodySmall.copy(fontSize = 18.sp),
                    color = Color.Red,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_next),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingScreenPreview() {
    SemonemoTheme {
        SettingScreen()
    }
}
