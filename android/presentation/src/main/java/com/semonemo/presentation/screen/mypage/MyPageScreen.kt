package com.semonemo.presentation.screen.mypage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.domain.model.User
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomDropdownMenu
import com.semonemo.presentation.component.CustomDropdownMenuStyles
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.NameWithBadge
import com.semonemo.presentation.component.SubScribeButton
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.component.TopAppBarNavigationType
import com.semonemo.presentation.screen.mypage.subscreen.MyPageLikedAssets
import com.semonemo.presentation.screen.mypage.subscreen.MyPageLikedFrames
import com.semonemo.presentation.screen.mypage.subscreen.MyPageOwnedAssets
import com.semonemo.presentation.screen.mypage.subscreen.MyPageOwnedFrames
import com.semonemo.presentation.screen.mypage.subscreen.MyPageSellFrames
import com.semonemo.presentation.theme.Blue3
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.toAbsolutePath
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@Composable
fun MyPageRoute(
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    navigateToFollowList: (String, List<User>, List<User>) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToAssetDetail: (Long) -> Unit,
    navigateToSaleFrameDetail: (Long) -> Unit,
    viewModel: MyPageViewModel = hiltViewModel(),
    onErrorSnackBar: (String) -> Unit,
    userId: Long,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    HandleMyPageEvent(
        uiEvent = viewModel.uiEvent,
        onErrorSnackBar = onErrorSnackBar,
        onSubscribe = {
            viewModel.loadOtherUserInfo()
            viewModel.loadComponents()
        },
    )
    HandleMyPageUi(
        modifier = modifier,
        uiState = uiState,
        navigateToDetail = navigateToDetail,
        navigateToFollowList = navigateToFollowList,
        navigateToSetting = navigateToSetting,
        navigateToAssetDetail = navigateToAssetDetail,
        updateProfileImage = { imageUri ->
            val image = File(imageUri.toAbsolutePath(context))
            viewModel.updateProfileImage(image, imageUri.toString())
        },
        followUser = {
            viewModel.followUser(userId)
        },
        unfollowUser = {
            viewModel.unfollowUser(userId)
        },
        navigateToSaleFrameDetail = navigateToSaleFrameDetail,
        userId = userId,
    )
}

@Composable
fun HandleMyPageEvent(
    uiEvent: SharedFlow<MyPageUiEvent>,
    onSubscribe: () -> Unit,
    onErrorSnackBar: (String) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is MyPageUiEvent.Error -> onErrorSnackBar(event.errorMessage)
                MyPageUiEvent.Subscribe -> onSubscribe()
            }
        }
    }
}

@Composable
fun HandleMyPageUi(
    modifier: Modifier = Modifier,
    uiState: MyPageUiState,
    navigateToDetail: (Long) -> Unit,
    navigateToFollowList: (String, List<User>, List<User>) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToAssetDetail: (Long) -> Unit,
    navigateToSaleFrameDetail: (Long) -> Unit,
    updateProfileImage: (Uri) -> Unit,
    followUser: () -> Unit,
    unfollowUser: () -> Unit,
    userId: Long,
) {
    when (uiState) {
        MyPageUiState.Loading -> LoadingDialog(modifier = Modifier.fillMaxSize())
        is MyPageUiState.Success ->
            MyPageScreen(
                modifier = modifier,
                navigateToDetail = navigateToDetail,
                navigateToFollowList = navigateToFollowList,
                navigateToSetting = navigateToSetting,
                navigateToAssetDetail = navigateToAssetDetail,
                nickname = uiState.nickname,
                profileImageUrl = uiState.profileImageUrl,
                follower = uiState.follower,
                following = uiState.following,
                updateProfileImage = updateProfileImage,
                isFollow = uiState.isFollow,
                followUser = followUser,
                unfollowUser = unfollowUser,
                frameList = uiState.frameList,
                sellFrameList = uiState.sellFrameList,
                assetList = uiState.assetList,
                likeAssets = uiState.likeAssets,
                likedFrames = uiState.likedFrames,
                navigateToSaleFrameDetail = navigateToSaleFrameDetail,
                userId = userId,
            )
    }
}

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit = {},
    navigateToFollowList: (String, List<User>, List<User>) -> Unit = { _, _, _ -> },
    navigateToSetting: () -> Unit = {},
    navigateToAssetDetail: (Long) -> Unit = {},
    navigateToSaleFrameDetail: (Long) -> Unit = {},
    nickname: String = "짜이한",
    profileImageUrl: String = "",
    follower: List<User> = emptyList(),
    following: List<User> = emptyList(),
    updateProfileImage: (Uri) -> Unit = {},
    isFollow: Boolean? = null,
    followUser: () -> Unit = {},
    unfollowUser: () -> Unit = {},
    frameList: List<MyFrame> = listOf(),
    sellFrameList: List<FrameDetail> = listOf(),
    assetList: List<Asset> = listOf(),
    likeAssets: List<SellAssetDetail> = listOf(),
    likedFrames: List<FrameDetail> = listOf(),
    userId: Long = -1L,
) {
    val tabs =
        if (userId == -1L) {
            listOf("프레임", "에셋", "찜")
        } else {
            listOf("프레임", "에셋")
        }
    val selectedIndex = remember { mutableIntStateOf(0) }
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    updateProfileImage(it)
                }
            },
        )

    var isSell by remember { mutableStateOf(false) }
    var likeCategory by remember { mutableStateOf("프레임") }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
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
                modifier = Modifier.fillMaxHeight(0.045f),
                title = {
                    NameWithBadge(
                        name = nickname,
                        size = 18,
                    )
                },
                navigationType = TopAppBarNavigationType.None,
                actionButtons = {
                    // 타사용자 페이지
                    isFollow?.let { isFollow ->
                        SubScribeButton(
                            isSubscribed = isFollow,
                            onToggleSubscription = {
                                if (isFollow) {
                                    unfollowUser()
                                } else {
                                    followUser()
                                }
                            },
                        )
                    } ?: run {
                        // 마이페이지
                        Icon(
                            modifier =
                                Modifier
                                    .size(23.dp)
                                    .noRippleClickable {
                                        navigateToSetting()
                                    },
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = "",
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            Box {
                GlideImage(
                    imageModel = profileImageUrl.toUri(),
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .size(120.dp)
                            .border(width = 1.5.dp, color = Gray03, shape = CircleShape)
                            .clip(shape = CircleShape),
                    loading = {
                        ImageLoadingProgress(
                            modifier = Modifier.fillMaxSize(),
                        )
                    },
                )
                if (isFollow == null) { // 마이페이지인 경우에만 버튼 나오도록
                    Image(
                        modifier =
                            Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 5.dp, bottom = 10.dp)
                                .background(Blue3, shape = CircleShape)
                                .size(20.dp)
                                .noRippleClickable {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                                        ),
                                    )
                                },
                        imageVector = Icons.Filled.Add,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "${frameList.size}",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "보유중",
                        style = Typography.labelSmall.copy(fontSize = 13.sp),
                    )
                }
                Column(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .weight(1f)
                            .noRippleClickable {
                                navigateToFollowList(nickname, follower, following)
                            },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "${follower.size}",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "팔로워",
                        style = Typography.labelSmall.copy(fontSize = 13.sp),
                    )
                }
                Column(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .weight(1f)
                            .noRippleClickable {
                                navigateToFollowList(nickname, follower, following)
                            },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "${following.size}",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "팔로잉",
                        style = Typography.labelSmall.copy(fontSize = 13.sp),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            CustomTab(
                tabList = tabs,
                selectedIndex = selectedIndex.intValue,
                onTabSelected = { tab -> selectedIndex.intValue = tab },
            )
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedContent(
                targetState = selectedIndex.intValue,
                transitionSpec = {
                    (
                        fadeIn(animationSpec = tween(durationMillis = 300)) +
                            slideInVertically(
                                animationSpec = tween(durationMillis = 300),
                            )
                    ).togetherWith(
                        fadeOut(animationSpec = tween(durationMillis = 300)) +
                            slideOutVertically(
                                animationSpec = tween(durationMillis = 300),
                            ),
                    )
                },
                label = "",
            ) { targetIndex ->
                when (targetIndex) {
                    0 -> {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 60.dp),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                CustomDropdownMenu(
                                    menuItems =
                                        listOf(
                                            "보유중" to {
                                                isSell = false
                                            },
                                            "판매중" to {
                                                isSell = true
                                            },
                                        ),
                                    styles =
                                        CustomDropdownMenuStyles(),
                                )
                            }
                            if (isSell) {
                                MyPageSellFrames(
                                    modifier = Modifier,
                                    sellFrameList = sellFrameList,
                                    navigateToDetail = navigateToDetail,
                                )
                            } else {
                                MyPageOwnedFrames(
                                    modifier = Modifier,
                                    frameList = frameList,
                                    navigateToDetail = navigateToDetail,
                                )
                            }
                        }
                    }

                    1 -> {
                        // 애셋
                        MyPageOwnedAssets(
                            modifier = Modifier.padding(bottom = 60.dp),
                            assetList = assetList,
                        )
                    }

                    2 -> {
                        // 찜
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 60.dp),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                CustomDropdownMenu(
                                    menuItems =
                                        listOf(
                                            "프레임" to {
                                                // 찜한 프레임 불러 오기
                                                likeCategory = "프레임"
                                            },
                                            "에셋" to {
                                                // 찜한 에셋 불러오기
                                                likeCategory = "에셋"
                                            },
                                        ),
                                    styles =
                                        CustomDropdownMenuStyles(),
                                )
                            }
                            when (likeCategory) {
                                "프레임" -> {
                                    MyPageLikedFrames(
                                        modifier = Modifier,
                                        likedFrames = likedFrames,
                                        navigateToSaleFrameDetail = navigateToSaleFrameDetail,
                                    )
                                }

                                "에셋" -> {
                                    MyPageLikedAssets(
                                        modifier = Modifier,
                                        likeAssets = likeAssets,
                                        navigateToAssetDetail = navigateToAssetDetail,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MyPageScreenPreview() {
    SemonemoTheme {
        MyPageScreen()
    }
}
