package com.semonemo.presentation

import BottomNavigationBar
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.semonemo.domain.model.User
import com.semonemo.presentation.navigation.CustomFAB
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.screen.aiAsset.AiAssetScreen
import com.semonemo.presentation.screen.aiAsset.AssetDoneRoute
import com.semonemo.presentation.screen.aiAsset.draw.DrawAssetRoute
import com.semonemo.presentation.screen.aiAsset.prompt.PromptRoute
import com.semonemo.presentation.screen.auction.AuctionDetailScreen
import com.semonemo.presentation.screen.auction.AuctionScreen
import com.semonemo.presentation.screen.auction.register.AuctionRegisterRoute
import com.semonemo.presentation.screen.coin.CoinRoute
import com.semonemo.presentation.screen.detail.asset.AssetDetailRoute
import com.semonemo.presentation.screen.detail.frame.FrameDetailRoute
import com.semonemo.presentation.screen.frame.MomentGraph
import com.semonemo.presentation.screen.imgAsset.ImageAssetScreen
import com.semonemo.presentation.screen.imgAsset.ImageSelectRoute
import com.semonemo.presentation.screen.login.LoginRoute
import com.semonemo.presentation.screen.mypage.FollowListScreen
import com.semonemo.presentation.screen.mypage.MyPageRoute
import com.semonemo.presentation.screen.mypage.detail.DetailRoute
import com.semonemo.presentation.screen.mypage.setting.SettingRoute
import com.semonemo.presentation.screen.picture.PictureGraph
import com.semonemo.presentation.screen.search.SearchRoute
import com.semonemo.presentation.screen.signup.SignUpRoute
import com.semonemo.presentation.screen.store.StoreFullViewScreen
import com.semonemo.presentation.screen.store.StoreRoute
import com.semonemo.presentation.screen.store.asset.AssetSaleRoute
import com.semonemo.presentation.screen.store.frame.FrameSaleRoute
import com.semonemo.presentation.screen.wallet.WalletRoute
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.toUriOrDefault
import kotlinx.coroutines.launch

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val (visible, setVisible) =
        remember {
            mutableStateOf(false)
        }
    when (currentRoute) {
        "mypage/{userId}", "shop", "moment", "wallet", "auction", "storeFullView/{isFrame}" ->
            setVisible(
                true,
            )

        else -> setVisible(false)
    }

    val snackBarHostState =
        remember {
            SnackbarHostState()
        }

    val onShowErrorSnackBar: (errorMessage: String) -> Unit = { errorMessage ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(errorMessage)
        }
    }

    val actionWithSnackBar: (Uri) -> Unit = { imageUri ->
        coroutineScope.launch {
            snackBarHostState
                .showSnackbar(
                    message = "이미지가 저장되었습니다!",
                    actionLabel = "확인하기!",
                    duration = SnackbarDuration.Short,
                ).let { result ->
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            val intent =
                                Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(imageUri, "image/jpeg")
                                }
                            context.startActivity(intent)
                        }

                        SnackbarResult.Dismissed -> {
                        }
                    }
                }
        }
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            if (visible) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (visible) {
                systemUiController.setNavigationBarColor(color = Color.White)
                val isSelected = currentRoute == ScreenDestinations.Moment.route
                CustomFAB(
                    onClick = {
                        navController.navigate(ScreenDestinations.Moment.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    icon =
                        if (isSelected) {
                            R.drawable.ic_bot_nav_fill_frame
                        } else {
                            R.drawable.ic_bot_nav_outline_frame
                        },
                    iconColor = Color.White,
                    labelColor =
                        if (isSelected) GunMetal else Gray01,
                    labelStyle =
                        if (isSelected) Typography.bodySmall.copy(fontSize = 12.sp) else Typography.labelSmall,
                )
            } else {
                systemUiController.setNavigationBarColor(color = Color.Transparent)
            }
        },
    ) { _ ->
        MainNavHost(
            navController = navController,
            startDestination = ScreenDestinations.Login.route,
            onShowErrorSnackBar = onShowErrorSnackBar,
            actionWithSnackBar = actionWithSnackBar,
        )
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    onShowErrorSnackBar: (String) -> Unit,
    actionWithSnackBar: (Uri) -> Unit,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            route = ScreenDestinations.Login.route,
        ) {
            LoginRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToRegister = { walletAddress ->
                    navController.navigate(ScreenDestinations.Register.createRoute(walletAddress))
                },
                onShowErrorSnackBar = onShowErrorSnackBar,
                navigateToMoment = {
                    navController.navigate(ScreenDestinations.Moment.route) {
                        popUpTo(startDestination) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.Register.route,
            arguments = ScreenDestinations.Register.arguments,
        ) {
            SignUpRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Shop.route,
        ) {
            StoreRoute(
                modifier = modifier,
                navigateToFullView = { isFrame ->
                    navController.navigate(
                        ScreenDestinations.StoreFullView.createRoute(
                            isFrame,
                        ),
                    )
                },
                navigateToSearch = {
                    navController.navigate(ScreenDestinations.Search.route)
                },
                navigateToAssetSale = {
                    navController.navigate(ScreenDestinations.AssetSale.route)
                },
                navigateToFrameSale = {
                    navController.navigate(ScreenDestinations.FrameSale.route)
                },
                navigateToFrameDetail = { marketId ->
                    navController.navigate(ScreenDestinations.FrameDetail.createRoute(marketId))
                },
                navigateToAssetDetail = { assetSellId ->
                    navController.navigate(ScreenDestinations.AssetDetail.createRoute(assetSellId))
                },
            )
        }

        composable(
            route = ScreenDestinations.Search.route,
        ) {
            SearchRoute(
                navigateToProfile = { userId ->
                    navController.navigate(ScreenDestinations.MyPage.createRoute(userId))
                },
                navigateToAssetDetail = { sellId ->
                    navController.navigate(ScreenDestinations.AssetDetail.createRoute(sellId))
                },
                navigateToFrameDetail = { nftId ->
                    navController.navigate(ScreenDestinations.FrameDetail.createRoute(nftId))
                },
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.Auction.route,
        ) {
            AuctionScreen(
                modifier = modifier,
                navigateToAuctionDetail = { auctionId ->
                    navController.navigate(
                        ScreenDestinations.AuctionDetail.createRoute(
                            auctionId,
                        ),
                    )
                },
                navigateToAuctionRegister = {
                    navController.navigate(ScreenDestinations.AuctionRegister.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Wallet.route,
        ) {
            WalletRoute(
                modifier = modifier,
                navigateToCoinDetail = {
                    navController.navigate(ScreenDestinations.CoinDetail.route)
                },
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.MyPage.route,
            arguments = ScreenDestinations.MyPage.arguments,
        ) { navBackStackEntry ->
            val userId = navBackStackEntry.arguments?.getLong("userId")

            MyPageRoute(
                modifier = modifier,
                navigateToDetail = { id ->
                    navController.navigate(
                        ScreenDestinations.Detail.createRoute(id),
                    )
                },
                navigateToFollowList = { nickname, followerList, followingList, selectedTabIndex ->
                    navController.navigate(
                        ScreenDestinations.FollowList.createRoute(
                            nickname = nickname,
                            followerList = followerList,
                            followingList = followingList,
                            selectedTabIndex = selectedTabIndex,
                        ),
                    )
                },
                navigateToSetting = {
                    navController.navigate(ScreenDestinations.Setting.route)
                },
                navigateToAssetDetail = { assetSellId ->
                    navController.navigate(ScreenDestinations.AssetDetail.createRoute(assetSellId))
                },
                navigateToSaleFrameDetail = { marketId ->
                    navController.navigate(ScreenDestinations.FrameDetail.createRoute(marketId))
                },
                onErrorSnackBar = onShowErrorSnackBar,
                userId = userId ?: -1,
            )
        }

        composable(
            route = ScreenDestinations.Setting.route,
        ) {
            SettingRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToLogin = {
                    navController.navigate(ScreenDestinations.Login.route)
                },
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.FollowList.route,
            arguments = ScreenDestinations.FollowList.arguments,
        ) { navBackStackEntry ->
            val nickname = navBackStackEntry.arguments?.getString("nickname")
            val followerList =
                navBackStackEntry.arguments?.getParcelableArrayList<User>("followerList")
            val followingList =
                navBackStackEntry.arguments?.getParcelableArrayList<User>("followingList")
            val selectedTabIndex =
                navBackStackEntry.arguments?.getInt("selectedTabIndex") ?: 0

            FollowListScreen(
                modifier = modifier,
                nickname = nickname ?: "",
                popUpBackStack = navController::popBackStack,
                navigateToProfile = {
                    navController.navigate(ScreenDestinations.MyPage.createRoute(it))
                },
                followerList = followerList ?: emptyList(),
                followingList = followingList ?: emptyList(),
                selectedTabIndex = selectedTabIndex,
            )
        }

        composable(
            route = ScreenDestinations.ImageAsset.route,
        ) {
            ImageAssetScreen(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToSelect = { selectedImg ->
                    navController.navigate(ScreenDestinations.Select.createRoute(selectedImg))
                },
            )
        }

        composable(
            route = ScreenDestinations.Select.route,
            arguments = ScreenDestinations.Select.arguments,
        ) { navBackStackEntry ->
            val imageUriString = navBackStackEntry.arguments?.getString("selectedImg")
            val imageUri = imageUriString?.toUriOrDefault()
            ImageSelectRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToDone = { assetUrl ->
                    navController.navigate(ScreenDestinations.AssetDone.createRoute(assetUrl)) {
                        popUpTo("imageAsset") { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.AssetDone.route,
            arguments = ScreenDestinations.AssetDone.arguments,
        ) {
            AssetDoneRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToMy = {
                    navController.navigate(ScreenDestinations.MyPage.createRoute(it))
                },
                onErrorSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.AiAsset.route,
        ) {
            AiAssetScreen(
                modifier = modifier,
                navigateToDraw = {
                    navController.navigate(ScreenDestinations.DrawAsset.route)
                },
                navigateToPrompt = {
                    navController.navigate(ScreenDestinations.PromptAsset.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.DrawAsset.route,
        ) {
            DrawAssetRoute(
                modifier = modifier,
                navigateToDone = { assetUrl ->
                    navController.navigate(ScreenDestinations.AssetDone.createRoute(assetUrl)) {
                        popUpTo("imageAsset") { inclusive = true }
                    }
                },
                popUpToBackStack = navController::popBackStack,
                onErrorSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.PromptAsset.route,
        ) {
            PromptRoute(
                modifier = modifier,
                navigateToDone = {
                    navController.navigate(
                        ScreenDestinations.AssetDone.createRoute(
                            it,
                        ),
                    )
                },
                popUpBackStack = navController::popBackStack,
                onErrorSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Detail.route,
            arguments = ScreenDestinations.Detail.arguments,
        ) {
            DetailRoute(
                modifier = modifier,
                onShowSnackBar = onShowErrorSnackBar,
                popUpBackStack = navController::popBackStack,
            )
        }

        MomentGraph(
            modifier = modifier,
            navController = navController,
            graphRoute = "frame_graph",
            onErrorSnackBar = onShowErrorSnackBar,
        )

        composable(
            route = ScreenDestinations.AuctionDetail.route,
            arguments = ScreenDestinations.AuctionDetail.arguments,
        ) {
            AuctionDetailScreen(
                modifier = modifier,
                auctionId = it.arguments?.getLong("auctionId") ?: -1L,
                registerId = it.arguments?.getLong("registerId") ?: -1L,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.StoreFullView.route,
            arguments = ScreenDestinations.StoreFullView.arguments,
        ) { navBackStackEntry ->
            StoreFullViewScreen(
                modifier = modifier,
                isFrame = navBackStackEntry.arguments?.getBoolean("isFrame") ?: false,
                popUpBackStack = navController::popBackStack,
                navigateToAssetSale = { navController.navigate(ScreenDestinations.AssetSale.route) },
                navigateToFrameSale = { navController.navigate(ScreenDestinations.FrameSale.route) },
                navigateToAssetDetail = {
                    navController.navigate(
                        ScreenDestinations.AssetDetail.createRoute(
                            it,
                        ),
                    )
                },
                navigateToFrameDetail = {
                    navController.navigate(
                        ScreenDestinations.FrameDetail.createRoute(
                            it,
                        ),
                    )
                },
                onShowErrorSnackBar = { onShowErrorSnackBar(it) },
            )
        }

        composable(
            route = ScreenDestinations.AuctionRegister.route,
        ) {
            AuctionRegisterRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.FrameSale.route,
        ) {
            FrameSaleRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        PictureGraph(
            modifier = modifier,
            navController = navController,
            onErrorSnackBar = onShowErrorSnackBar,
            actionWithSnackBar = actionWithSnackBar,
            graphRoute = "picture_graph",
        )

        composable(
            route = ScreenDestinations.AssetSale.route,
        ) {
            AssetSaleRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.FrameDetail.route,
            arguments = ScreenDestinations.FrameDetail.arguments,
        ) {
            FrameDetailRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
                navigateToDetail = {
                    navController.navigate(ScreenDestinations.FrameDetail.createRoute(it))
                },
                navigateToProfile = {
                    navController.navigate(ScreenDestinations.MyPage.createRoute(it))
                },
            )
        }

        composable(
            route = ScreenDestinations.AssetDetail.route,
            arguments = ScreenDestinations.AssetDetail.arguments,
        ) {
            AssetDetailRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
                navigateToDetail = {
                    navController.navigate(ScreenDestinations.AssetDetail.createRoute(it))
                },
            )
        }

        composable(
            route = ScreenDestinations.CoinDetail.route,
        ) {
            CoinRoute(
                modifier = modifier,
                onShowSnackBar = onShowErrorSnackBar,
                popUpBackStack = navController::popBackStack,
            )
        }
    }
}
